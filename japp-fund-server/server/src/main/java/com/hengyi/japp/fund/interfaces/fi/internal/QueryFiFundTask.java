package com.hengyi.japp.fund.interfaces.fi.internal;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.interfaces.fi.domain.Account;
import com.hengyi.japp.fund.interfaces.fi.domain.AccountFund;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryFiFundTask extends RecursiveTask<Stream<AccountFund>> {
    public static final double DEFAULT_MIN_THRESHOLD = 1000000;
    private final Function<String, Stream<Map<String, Object>>> querySqlFun;
    private final Map<Object, Account> accountMap;
    private final LocalDate ldCur = LocalDate.now();
    private final LocalDate ldStart;
    private final LocalDate ldEnd;

    private final String sqlTpl;

    public QueryFiFundTask(Function<String, Stream<Map<String, Object>>> querySqlFun, Map<Object, Account> accountMap, LocalDate ldStart, LocalDate ldEnd) {
        this.querySqlFun = querySqlFun;
        this.accountMap = accountMap;
        this.ldStart = ldStart;
        this.ldEnd = ldEnd;
        this.sqlTpl = "SELECT * FROM ${table} WHERE ACCOUNTID=${accountId} AND TO_CHAR(TRANSACTIONTIME,'yyyy-mm-dd')>='" + ldStart + "' AND TO_CHAR(TRANSACTIONTIME,'yyyy-mm-dd')<='" + ldEnd + "' AND ISDELETEDBYBANK=0";
    }

    @Override
    protected Stream<AccountFund> compute() {
        if (J.isEmpty(accountMap)) {
            return Stream.empty();
        }

        final Single[] singleTasks = accountMap.values()
                .stream()
                .parallel()
                .map(Single::new)
                .toArray(Single[]::new);
        invokeAll(singleTasks);

        return Stream.of(singleTasks)
                .flatMap(Single::join)
                // 合并日执行，小于 DEFAULT_MIN_THRESHOLD 的全部当做一笔
                .collect(Collectors.groupingBy(AccountFund::triple))
                .values()
                .stream()
                .parallel()
                .flatMap(funds -> {
                    // 计算合并
                    Stream<AccountFund> streamSum = funds.stream()
                            .parallel()
                            .filter(fund -> fund.getMoney().abs().doubleValue() < DEFAULT_MIN_THRESHOLD)
                            .map(AccountFund::getMoney)
                            .collect(Collectors.toMap(v -> v.doubleValue() > 0 ? "+" : "-", Function.identity(), (a, b) -> a.add(b)))
                            .values()
                            .stream()
                            .parallel()
                            .map(money -> new AccountFund(funds.get(0), money));

                    // 小于 minThreshold 阀值的会被合并，所以需要过滤掉
                    Stream<AccountFund> stream = funds.stream()
                            .filter(fund -> fund.getMoney().abs().doubleValue() >= DEFAULT_MIN_THRESHOLD);
                    return Stream.concat(streamSum, stream);
                });
    }

    private class Single extends RecursiveTask<Stream<AccountFund>> {
        private final Account account;

        private Single(Account account) {
            this.account = account;
        }

        @Override
        protected Stream<AccountFund> compute() {
            Stream<AccountFund> streamCur = ldEnd.isBefore(ldCur) ? Stream.empty() : query("BP_ACCTCURTRANSINFO");
            Stream<AccountFund> streamHis = ldStart.isAfter(ldCur) ? Stream.empty() : query("BP_ACCTHISTRANSINFO");
            return Stream.concat(streamCur, streamHis);
        }

        private Stream<AccountFund> query(String table) {
            final String accountId = account.getId().toString();
            String sql = J.strTpl(sqlTpl, ImmutableMap.of("table", table, "accountId", accountId));
            return querySqlFun.apply(sql)
                    .parallel()
                    .map(map -> new AccountFund(map, accountMap));
        }
    }

}
