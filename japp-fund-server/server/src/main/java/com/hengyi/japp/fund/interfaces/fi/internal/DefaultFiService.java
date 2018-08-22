package com.hengyi.japp.fund.interfaces.fi.internal;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.Constant;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.interfaces.fi.CorporationSuggest;
import com.hengyi.japp.fund.interfaces.fi.CurrencySuggest;
import com.hengyi.japp.fund.interfaces.fi.FiService;
import com.hengyi.japp.fund.interfaces.fi.domain.Account;
import com.hengyi.japp.fund.interfaces.fi.domain.AccountFund;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-12-3.
 */
@Stateless
public class DefaultFiService implements FiService {
    @Inject
    private Logger log;
    @Resource(lookup = Constant.FI_DS)
    private DataSource fiDS;
    private final Function<String, Stream<Map<String, Object>>> querySqlFun = sql -> {
        try {
            return new QueryRunner(fiDS).query(sql, new MapListHandler()).stream();
        } catch (SQLException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    };
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;

    /**
     * 结束日期在当天之后，肯定会有一些天的余额需要重新通过 当前余额和日计划累计来计算
     * 余额的获取必须完整，如果当天是 1月1日，那么查2月，3月的数据也必须补充完全
     *
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @return
     */
    @Override
    public Stream<? extends Balancelike> queryBalance(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        Map<Object, Account> accountMap = queryAccountMap(corporations, currencies);
        if (J.isEmpty(accountMap)) {
            return Stream.iterate(ldStart, it -> it.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                    .parallel()
                    .flatMap(ld -> corporations.stream()
                            .parallel()
                            .flatMap(corporation -> currencies.stream()
                                    .parallel()
                                    .filter(currency -> !currency.isCdhp())
                                    .map(currency -> {
                                        FundBalance ba = new FundBalance();
                                        ba.setCorporation(corporation);
                                        ba.setCurrency(currency);
                                        ba.setDate(J.date(ld));
                                        ba.setBalance(BigDecimal.ZERO);
                                        return ba;
                                    })
                            )
                    );
        }
        final QueryFiBalanceTask task = new QueryFiBalanceTask(querySqlFun, accountMap, ldStart, ldEnd);
        return ForkJoinPool.commonPool().invoke(task);
    }


    @Override
    public Stream<AccountFund> queryFund(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        Map<Object, Account> accountMap = queryAccountMap(corporations, currencies);
        if (J.isEmpty(accountMap)) {
            return Stream.empty();
        }
        final QueryFiFundTask task = new QueryFiFundTask(querySqlFun, accountMap, ldStart, ldEnd);
        return ForkJoinPool.commonPool().invoke(task);
    }

    // 公司--银行账户的map
    private Map<Object, Account> queryAccountMap(Set<Corporation> corporations, Set<Currency> currencies) {
        QueryFiAccountTask task = new QueryFiAccountTask(querySqlFun, corporations, currencies);
        return ForkJoinPool.commonPool().invoke(task);
    }

    @Override
    public Collection<CorporationSuggest> suggestCorporations(String q) {
        final String sqlTpl = "SELECT * FROM SYS_AGENCY WHERE AGENCYCODE LIKE '%${q}%'";
        Collection<String> importedCodes = corporationRepository.findAll()
                .map(Corporation::getCode)
                .collect(Collectors.toSet());
        final String sql = J.strTpl(sqlTpl, ImmutableMap.of("q", J.defaultString(q)));
        return querySqlFun.apply(sql)
                .map(CorporationSuggest::new)
                .filter(it -> !importedCodes.contains(it.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public CorporationSuggest findCorporationSuggest(long id) {
        final String sqlTpl = "SELECT * FROM SYS_AGENCY WHERE ID=${id}";
        String sql = J.strTpl(sqlTpl, ImmutableMap.of("id", "" + id));
        return querySqlFun.apply(sql)
                .map(CorporationSuggest::new)
                .findFirst()
                .get();
    }

    @Override
    public Collection<CurrencySuggest> suggestCurrencies(final String q) {
        final String sqlTpl = "SELECT * FROM SYS_DATADICTIONARY_VALUE WHERE DICTTYPE='Currency' AND DICTCODE LIKE '%${q}%'";
        final String sql = J.strTpl(sqlTpl, ImmutableMap.of("q", J.defaultString(q)));
        Collection<String> importedCodes = currencyRepository.findAll()
                .parallel()
                .map(Currency::getCode)
                .collect(Collectors.toSet());
        Predicate<CurrencySuggest> predicate = o -> !importedCodes.contains(o.getCode());
        return querySqlFun.apply(sql)
                .parallel()
                .map(CurrencySuggest::new)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencySuggest findCurrencySuggest(String code) {
        final String sqlTpl = "SELECT * FROM SYS_DATADICTIONARY_VALUE WHERE DICTTYPE='Currency' AND DICTCODE='${code}'";
        final String sql = J.strTpl(sqlTpl, ImmutableMap.of("code", code));
        return querySqlFun.apply(sql)
                .map(CurrencySuggest::new)
                .findFirst()
                .get();
    }

}
