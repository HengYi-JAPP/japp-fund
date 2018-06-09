package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.CalculateFundBalanceService;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.domain.permission.RoleType;
import com.hengyi.japp.fund.domain.repository.FundRepository;
import com.hengyi.japp.fund.domain.repository.MonthFundPlanRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class FundService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private FundRepository fundRepository;
    @Inject
    private MonthFundPlanRepository monthFundPlanRepository;
    @Inject
    private AuthService authService;
    @Inject
    private CalculateFundBalanceService calculateFundBalanceService;
    @Inject
    private ApplicationEvents applicationEvents;

    public Fund create(Principal principal, String monthFundPlanId, FundlikeUpdateCommand command) throws Exception {
        Fund fund = save(principal, monthFundPlanId, new Fund(), command);
        calculateFundBalanceService.reCalculateByCreate(fund);
        applicationEvents.curdEvent(principal, Fund.class, fund.getId(), EventType.CREATE, command);
        return fund;
    }

    private Fund save(Principal principal, String monthFundPlanId, Fund fund, FundlikeUpdateCommand command) throws Exception {
        MonthFundPlan monthFundPlan = monthFundPlanRepository.find(monthFundPlanId);
        Corporation corporation = monthFundPlan.getCorporation();
        Currency currency = monthFundPlan.getCurrency();
        if (!currency.isCdhp()) {
            throw new RuntimeException();
        }
        authService.checkPermission(principal, corporation, currency, RoleType.FUND_UPDATE);

        fund.setMonthFundPlan(monthFundPlan);
        fund.setCorporation(corporation);
        fund.setCurrency(currency);
        fund.setPurpose(monthFundPlan.getPurpose());
        //todo 检查日期范围是否在月计划当中
        fund.setDate(command.getDate());
        fund.setMoney(command.getMoney());
        fund.setNote(command.getNote());
        Operator operator = operatorRepository.find(principal.getName());
        fund._operator(operator);
        return fundRepository.save(fund);
    }

    public Fund update(Principal principal, String monthFundPlanId, String id, FundlikeUpdateCommand command) throws Exception {
        Fund oldFund = fundRepository.find(id);
        Date oldDate = oldFund.getDate();
        BigDecimal oldMoney = oldFund.getMoney();
        Fund fund = save(principal, monthFundPlanId, oldFund, command);
        calculateFundBalanceService.reCalculateByUpdate(fund, oldDate, oldMoney);
        applicationEvents.curdEvent(principal, Fund.class, fund.getId(), EventType.UPDATE, command);
        return fund;
    }

    public void delete(Principal principal, String id) throws Exception {
        Fund fund = fundRepository.find(id);
        if (fund.isDeleted()) {
            return;
        }
        Corporation corporation = fund.getCorporation();
        Currency currency = fund.getCurrency();
        authService.checkPermission(principal, corporation, currency, RoleType.FUND_UPDATE);
        fundRepository.delete(id);
        calculateFundBalanceService.reCalculateByDelete(fund);
        applicationEvents.curdEvent(principal, Fund.class, fund.getId(), EventType.DELETE);
    }

}
