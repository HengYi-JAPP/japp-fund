package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.MonthFundPlan;
import com.hengyi.japp.fund.share.CURDEntityRepository;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-20.
 */
public interface MonthFundPlanRepository extends CURDEntityRepository<MonthFundPlan, String> {
    @Override
    MonthFundPlan save(MonthFundPlan monthFundPlan);

    Stream<MonthFundPlan> query(Set<Corporation> corporations, Set<Currency> currencies, int year);

    Stream<MonthFundPlan> query(Set<Corporation> corporations, Set<Currency> currencies, int year, int month);

}
