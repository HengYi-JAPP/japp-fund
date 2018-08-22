package com.hengyi.japp.fund.interfaces.fi.internal;

import com.github.ixtf.japp.core.J;
import com.github.ixtf.japp.ee.Jee;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.repository.DayFundPlanRepository;
import com.hengyi.japp.fund.interfaces.fi.domain.Account;
import com.hengyi.japp.fund.interfaces.fi.domain.AccountBalance;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryFiBalanceTask extends RecursiveTask<Stream<? extends Balancelike>> {
    private final Function<String, Stream<Map<String, Object>>> querySqlFun;
    private final Map<Object, Account> accountMap;
    private final Set<Corporation> corporations;
    private final Set<Currency> currencies;
    private final LocalDate ldCur = LocalDate.now();
    private final LocalDate ldStart;
    private final LocalDate ldEnd;

    private final String curSqlTpl = "SELECT * FROM BP_ACCTCURBALANCE WHERE ACCOUNTID=${accountId}";
    private final String hisSqlTpl;

    public QueryFiBalanceTask(Function<String, Stream<Map<String, Object>>> querySqlFun, Map<Object, Account> accountMap, LocalDate ldStart, LocalDate ldEnd) {
        this.querySqlFun = querySqlFun;
        this.ldStart = ldStart;
        this.ldEnd = ldEnd;
        this.accountMap = accountMap;
        corporations = accountMap.values()
                .stream()
                .parallel()
                .map(Account::getCorporation)
                .collect(Collectors.toSet());
        currencies = accountMap.values()
                .stream()
                .parallel()
                .map(Account::getCurrency)
                .collect(Collectors.toSet());

        this.hisSqlTpl = "SELECT * FROM BP_ACCTHISBALANCE WHERE ACCOUNTID=${accountId} AND TO_CHAR(BALANCEDATE,'yyyy-mm-dd')>='" + this.ldStart + "' AND TO_CHAR(BALANCEDATE,'yyyy-mm-dd')<='" + this.ldEnd + "'";
    }

    @Override
    protected Stream<? extends Balancelike> compute() {
        final SumDayFundPlanTask sumDayFundPlanTask = new SumDayFundPlanTask();
        sumDayFundPlanTask.fork();

        final Single[] singleTasks = accountMap.values()
                .stream()
                .map(Single::new)
                .toArray(Single[]::new);
        invokeAll(singleTasks);

        final Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> balanceMap = Stream.of(singleTasks)
                .flatMap(Single::join)
                .collect(Collectors.toMap(AccountBalance::triple, AccountBalance::getBalance, (a, b) -> a.add(b)));

        return Stream.iterate(ldStart, it -> it.plusDays(1))
                .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                .parallel()
                .flatMap(ld -> corporations.stream()
                        .parallel()
                        .flatMap(corporation -> currencies.stream()
                                .parallel()
                                .map(currency -> getBalance(corporation, currency, ld, balanceMap, sumDayFundPlanTask.join()))
                        )
                );
    }

    private FundBalance getBalance(final Corporation corporation, final Currency currency, final LocalDate ld, Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> balanceMap, Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> sumByDayMap) {
        FundBalance result = new FundBalance();
        result.setCorporation(corporation);
        result.setCurrency(currency);
        result.setDate(J.date(ld));

        final Triple<Corporation, Currency, LocalDate> key = Triple.of(corporation, currency, ld);
        BigDecimal balance = Optional.ofNullable(balanceMap.get(key))
                .orElseGet(() -> {
                    BigDecimal curBalance = balanceMap.get(Triple.of(corporation, currency, ldCur));
                    // 会出现，公司和币种对应不上的情况，如果存在就过滤，比如：石化 日元 等等情况
                    if (curBalance == null) {
                        return BigDecimal.ZERO;
                    }

                    double sum = sumByDayMap.entrySet()
                            .stream()
                            .parallel()
                            .filter(entry -> {
                                Triple<Corporation, Currency, LocalDate> sumDayKey = entry.getKey();
                                LocalDate sumDayLd = sumDayKey.getRight();
                                return corporation.equals(sumDayKey.getLeft()) &&
                                        currency.equals(sumDayKey.getMiddle()) &&
                                        sumDayLd.isAfter(ldCur) &&
                                        !sumDayLd.isAfter(ld);
                            })
                            .map(Map.Entry::getValue)
                            .mapToDouble(BigDecimal::doubleValue)
                            .sum();
                    return curBalance.add(BigDecimal.valueOf(sum));
                });
        result.setBalance(balance);
        return result;
    }

    private class SumDayFundPlanTask extends RecursiveTask<Map<Triple<Corporation, Currency, LocalDate>, BigDecimal>> {
        @Override
        protected Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> compute() {
            // 结束日期大于当天，必将有些天的余额查不到，需要扩充
            if (ldEnd.isAfter(ldCur)) {
                // 累计每天的流水总金额，用于计算扩展的余额
                DayFundPlanRepository dayFundPlanRepository = Jee.getBean(DayFundPlanRepository.class);
                return dayFundPlanRepository.query(corporations, currencies, ldCur.plusDays(1), ldEnd)
                        .parallel()
                        .collect(Collectors.toMap(DayFundPlan::triple, DayFundPlan::getMoney, (a, b) -> a.add(b)));
            }
            return Maps.newHashMap();
        }
    }

    private class Single extends RecursiveTask<Stream<AccountBalance>> {
        private final Account account;

        private Single(Account account) {
            this.account = account;
        }

        @Override
        protected Stream<AccountBalance> compute() {
            final LocalDate ldCur = LocalDate.now();
            // 查询当前余额
            Stream<AccountBalance> streamCur = ldEnd.isBefore(ldCur) ? Stream.empty() : query(curSqlTpl);
            // 查询历史余额
            Stream<AccountBalance> streamHis = ldStart.isAfter(ldCur) ? Stream.empty() : query(hisSqlTpl);
            return Stream.concat(streamCur, streamHis);
        }

        private Stream<AccountBalance> query(String sqlTpl) {
            final String accountId = account.getId().toString();
            String sql = J.strTpl(sqlTpl, ImmutableMap.of("accountId", accountId));
            return querySqlFun.apply(sql)
                    .parallel()
                    .map(map -> new AccountBalance(map, accountMap));
        }
    }

}
