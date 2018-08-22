package com.hengyi.japp.fund.interfaces.fi.internal;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.interfaces.fi.domain.Account;
import com.hengyi.japp.fund.interfaces.fi.domain.FundProperty;
import com.hengyi.japp.fund.interfaces.fi.domain.SysAgency;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.Constant.CDHP;

public class QueryFiAccountTask extends RecursiveTask<Map<Object, Account>> {
    private final Function<String, Stream<Map<String, Object>>> querySqlFun;
    private final RecursiveTask<Map<String, SysAgency>> sysAgencyMapTask;
    private final RecursiveTask<Map<String, FundProperty>> fundPropertyMapTask;
    private final Map<String, Currency> currencyMap;

    public QueryFiAccountTask(Function<String, Stream<Map<String, Object>>> querySqlFun, Set<Corporation> corporations, Set<Currency> currencies) {
        this.querySqlFun = querySqlFun;
        fundPropertyMapTask = queryFundPropertyMapTask();
        sysAgencyMapTask = querySysAgencyMapTask(corporations);
        currencyMap = currencies.stream()
                .parallel()
                .distinct()
                // 承兑汇票使用本地查询
                .filter(currency -> !CDHP.equals(currency.getCode()))
                .collect(Collectors.toMap(Currency::getCode, Function.identity()));
    }

    @Override
    protected Map<Object, Account> compute() {
        if (J.isEmpty(currencyMap)) {
            return Maps.newConcurrentMap();
        }

        invokeAll(sysAgencyMapTask, fundPropertyMapTask);
        final Map<String, SysAgency> sysAgencyMap = sysAgencyMapTask.join();
        final Map<String, FundProperty> fundPropertyMap = fundPropertyMapTask.join();
        final String belongs = String.join(",", sysAgencyMap.keySet());
        final String sqlTpl = "SELECT * FROM TS_ACCOUNT WHERE ID_BELONGORG IN (" + belongs + ") AND CURRENCYCODE='${currencyCode}' AND STATUS<>0";

        final RecursiveTask<Stream<Account>>[] singleTasks = currencyMap.keySet()
                .stream()
                .parallel()
                .map(currencyCode -> new RecursiveTask<Stream<Account>>() {
                    @Override
                    protected Stream<Account> compute() {
                        final String sql = J.strTpl(sqlTpl, ImmutableMap.of("currencyCode", currencyCode));
                        return querySqlFun.apply(sql)
                                .parallel()
                                .map(map -> new Account(map, currencyMap, sysAgencyMap, fundPropertyMap))
                                .filter(QueryFiAccountTask.this::filter);
                    }
                })
                .toArray(RecursiveTask[]::new);
        invokeAll(singleTasks);

        return Stream.of(singleTasks)
                .flatMap(RecursiveTask::join)
                .collect(Collectors.toMap(Account::getId, Function.identity()));
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

    private RecursiveTask<Map<String, SysAgency>> querySysAgencyMapTask(Set<Corporation> corporations) {
        return new RecursiveTask<Map<String, SysAgency>>() {
            @Override
            protected Map<String, SysAgency> compute() {
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
        };
    }

    private RecursiveTask<Map<String, FundProperty>> queryFundPropertyMapTask() {
        return new RecursiveTask<Map<String, FundProperty>>() {
            @Override
            protected Map<String, FundProperty> compute() {
                final String sql = "SELECT * FROM SYS_DATADICTIONARY_VALUE WHERE DICTTYPE='FundProperty'";
                return querySqlFun.apply(sql)
                        .parallel()
                        .map(FundProperty::new)
                        .collect(Collectors.toMap(FundProperty::getCode, Function.identity()));
            }
        };
    }

}
