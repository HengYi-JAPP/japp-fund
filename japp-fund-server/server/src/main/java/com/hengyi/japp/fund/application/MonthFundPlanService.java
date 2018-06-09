package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.MonthFundPlanUpdateCommand;
import com.hengyi.japp.fund.domain.MonthFundPlan;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Stream;

public interface MonthFundPlanService {
    MonthFundPlan create(Principal principal, MonthFundPlanUpdateCommand command) throws Exception;

    MonthFundPlan update(Principal principal, String id, MonthFundPlanUpdateCommand command) throws Exception;

    void delete(Principal principal, String id) throws Exception;

    /**
     * 解决每次都要先创建月计划的问题，暂时先每次取月计划都返回数据
     *
     * @param corporationIds
     * @param currencyIds
     * @param year
     * @param month
     * @return
     */
    Stream<MonthFundPlan> listAnaywary(Principal principal, Collection<String> corporationIds, Collection<String> currencyIds, int year, int month);
}
