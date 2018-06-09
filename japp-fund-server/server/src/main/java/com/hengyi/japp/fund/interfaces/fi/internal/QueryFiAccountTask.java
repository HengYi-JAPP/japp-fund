package com.hengyi.japp.fund.interfaces.fi.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.interfaces.fi.domain.Account;
import com.hengyi.japp.fund.interfaces.fi.domain.FundProperty;
import com.hengyi.japp.fund.interfaces.fi.domain.SysAgency;
import org.jzb.J;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.Constant.CDHP;

public class QueryFiAccountTask extends RecursiveTask<Map<Object, Account>> {
    private final Function<String, Stream<Map<String, Object>>> querySqlFun;
    private final Map<String, SysAgency> sysAgencyMap;
    private final Map<String, FundProperty> fundPropertyMap;
    private final Map<String, Currency> currencyMap;
    private final String sqlTpl;

    public QueryFiAccountTask(Function<String, Stream<Map<String, Object>>> querySqlFun, Set<Corporation> corporations, Set<Currency> currencies) {
        this.querySqlFun = querySqlFun;
        sysAgencyMap = querySysAgencyMap(corporations);
        fundPropertyMap = queryFundPropertyMap();
        currencyMap = currencies.stream()
                .parallel()
                .distinct()
                // 承兑汇票使用本地查询
                .filter(currency -> !CDHP.equals(currency.getCode()))
                .collect(Collectors.toMap(Currency::getCode, Function.identity()));
        String sqlTpl = "SELECT * FROM TS_ACCOUNT WHERE ID_BELONGORG IN (${belongs}) AND CURRENCYCODE=${currencyCode} AND STATUS<>0";
        String belongs = String.join(",", sysAgencyMap.keySet());
        this.sqlTpl = J.strTpl(sqlTpl, ImmutableMap.of("belongs", belongs));
    }

    @Override
    protected Map<Object, Account> compute() {
        if (J.isEmpty(currencyMap)) {
            return Maps.newConcurrentMap();
        }

        return currencyMap.keySet()
                .stream()
                .parallel()
                .map(Single::new)
                .peek(Single::fork)
                .flatMap(Single::join)
                .collect(Collectors.toMap(Account::getId, Function.identity()));
    }

    private Map<String, SysAgency> querySysAgencyMap(Set<Corporation> corporations) {
        final String sqlTpl = "SELECT * FROM SYS_AGENCY WHERE AGENCYCODE IN (${codes})";
        Map<String, Corporation> corporationMap = corporations.stream()
                .parallel()
                .collect(Collectors.toMap(Corporation::getCode, Function.identity()));
        String codes = corporationMap.keySet()
                .stream()
                .parallel()
                .map(it -> "'" + it + "'")
                .collect(Collectors.joining(","));
        String sql = J.strTpl(sqlTpl, ImmutableMap.of("codes", codes));
        return querySqlFun.apply(sql)
                .parallel()
                .map(map -> new SysAgency(map, corporationMap))
                .collect(Collectors.toMap(SysAgency::getId, Function.identity()));
    }

    private Map<String, FundProperty> queryFundPropertyMap() {
        final String sql = "SELECT * FROM SYS_DATADICTIONARY_VALUE WHERE DICTTYPE='FundProperty'";
        return querySqlFun.apply(sql)
                .parallel()
                .map(FundProperty::new)
                .collect(Collectors.toMap(FundProperty::getCode, Function.identity()));
    }

    private class Single extends RecursiveTask<Stream<Account>> {
        private final String currencyCode;

        private Single(String currencyCode) {
            this.currencyCode = "'" + currencyCode + "'";
        }

        @Override
        protected Stream<Account> compute() {
            String sql = J.strTpl(sqlTpl, ImmutableMap.of("currencyCode", currencyCode));
            return querySqlFun.apply(sql)
                    .parallel()
                    .map(map -> new Account(map, currencyMap, sysAgencyMap, fundPropertyMap))
                    .filter(this::filter);
        }

        private boolean filter(Account account) {
            FundProperty fundProperty = account.getFundProperty();
            String fundPropertyCode = fundProperty.getCode();
            // 境外虚拟户不用显示
            if ("1035".equals(fundPropertyCode)) {
                return false;
            }
            String fundPropertyName = fundProperty.getName();
            switch (account.getCurrency().getCode()) {
                case "CNY": {
                    // 人民币  资金种类: -- 和 理财产品户
                    return "--".equals(fundPropertyName) || "理财产品户".equals(fundPropertyName);
                }
                default: {
                    // 非人民币  资金种类: 名称包含--保证金
                    return J.nonContains(fundPropertyName, "保证金");
                }
            }
        }
    }

}
