package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.domain.permission.RoleType;
import com.hengyi.japp.fund.domain.repository.DayFundPlanRepository;
import com.hengyi.japp.fund.domain.repository.MonthFundPlanRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class DayFundPlanService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private DayFundPlanRepository dayFundPlanRepository;
    @Inject
    private MonthFundPlanRepository monthFundPlanRepository;
    @Inject
    private AuthService authService;
    @Inject
    private ApplicationEvents applicationEvents;

    public DayFundPlan create(Principal principal, String monthFundPlanId, FundlikeUpdateCommand command) throws Exception {
        DayFundPlan fund = save(principal, monthFundPlanId, new DayFundPlan(), command);
        applicationEvents.curdEvent(principal, Fund.class, fund.getId(), EventType.CREATE, command);
        return fund;
    }

    private DayFundPlan save(Principal principal, String monthFundPlanId, DayFundPlan dayFundPlan, FundlikeUpdateCommand command) throws Exception {
        MonthFundPlan monthFundPlan = monthFundPlanRepository.find(monthFundPlanId);
        Corporation corporation = monthFundPlan.getCorporation();
        Currency currency = monthFundPlan.getCurrency();
        authService.checkPermission(principal, corporation, currency, RoleType.DAY_PLAN_UPDATE);

        dayFundPlan.setMonthFundPlan(monthFundPlan);
        dayFundPlan.setCorporation(corporation);
        dayFundPlan.setCurrency(currency);
        dayFundPlan.setPurpose(monthFundPlan.getPurpose());
        //todo 检查日期范围是否在月计划当中
        dayFundPlan.setDate(command.getDate());
        dayFundPlan.setMoney(command.getMoney());
        dayFundPlan.setNote(command.getNote());
        Operator operator = operatorRepository.find(principal.getName());
        dayFundPlan._operator(operator);
        return dayFundPlanRepository.save(dayFundPlan);
    }

    public DayFundPlan update(Principal principal, String monthFundPlanId, String id, FundlikeUpdateCommand command) throws Exception {
        DayFundPlan oldDayFundPlan = dayFundPlanRepository.find(id);
        DayFundPlan dayFundPlan = save(principal, monthFundPlanId, oldDayFundPlan, command);
        applicationEvents.curdEvent(principal, DayFundPlan.class, dayFundPlan.getId(), EventType.UPDATE, command);
        return dayFundPlan;
    }

    public void delete(Principal principal, String id) throws Exception {
        DayFundPlan dayFundPlan = dayFundPlanRepository.find(id);
        Corporation corporation = dayFundPlan.getCorporation();
        Currency currency = dayFundPlan.getCurrency();
        authService.checkPermission(principal, corporation, currency, RoleType.DAY_PLAN_UPDATE);
        dayFundPlanRepository.delete(id);
        applicationEvents.curdEvent(principal, DayFundPlan.class, dayFundPlan.getId(), EventType.DELETE);
    }

}
