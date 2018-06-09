package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.MonthFundPlanService;
import com.hengyi.japp.fund.application.command.MonthFundPlanUpdateCommand;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.domain.permission.RoleType;
import com.hengyi.japp.fund.domain.repository.*;
import org.jzb.J;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class MonthFundPlanServiceImpl implements MonthFundPlanService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private MonthFundPlanRepository monthFundPlanRepository;
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private PurposeRepository purposeRepository;
    @Inject
    private AuthService authService;
    @Inject
    private ApplicationEvents applicationEvents;

    @Override
    public MonthFundPlan create(Principal principal, MonthFundPlanUpdateCommand command) throws Exception {
        MonthFundPlan monthFundPlan = save(principal, new MonthFundPlan(), command);
        applicationEvents.curdEvent(principal, MonthFundPlan.class, monthFundPlan.getId(), EventType.CREATE, command);
        return monthFundPlan;
    }

    private MonthFundPlan save(Principal principal, MonthFundPlan monthFundPlan, MonthFundPlanUpdateCommand command) throws Exception {
        Corporation corporation = corporationRepository.find(command.getCorporation().getId());
        Currency currency = currencyRepository.find(command.getCurrency().getId());
        authService.checkPermission(principal, corporation, currency, RoleType.MONTH_PLAN_UPDATE);

        Purpose purpose = purposeRepository.find(command.getPurpose().getId());
        monthFundPlan.setCorporation(corporation);
        monthFundPlan.setCurrency(currency);
        monthFundPlan.setPurpose(purpose);
        monthFundPlan.setYear(command.getYear());
        monthFundPlan.setMonth(command.getMonth());
        monthFundPlan.setMoney(command.getMoney());
        monthFundPlan.setNote(command.getNote());

        Operator operator = operatorRepository.find(principal.getName());
        monthFundPlan._operator(operator);
        return monthFundPlanRepository.save(monthFundPlan);
    }

    @Override
    public MonthFundPlan update(Principal principal, String id, MonthFundPlanUpdateCommand command) throws Exception {
        MonthFundPlan oldMonthFundPlan = monthFundPlanRepository.find(id);
        MonthFundPlan monthFundPlan = save(principal, oldMonthFundPlan, command);
        applicationEvents.curdEvent(principal, MonthFundPlan.class, monthFundPlan.getId(), EventType.UPDATE, command);
        return monthFundPlan;
    }

    @Override
    public void delete(Principal principal, String id) throws Exception {
        MonthFundPlan monthFundPlan = monthFundPlanRepository.find(id);
        Corporation corporation = monthFundPlan.getCorporation();
        Currency currency = monthFundPlan.getCurrency();
        authService.checkPermission(principal, corporation, currency, RoleType.MONTH_PLAN_UPDATE);
        monthFundPlanRepository.delete(id);
        applicationEvents.curdEvent(principal, MonthFundPlan.class, monthFundPlan.getId(), EventType.DELETE);
    }

    @Override
    public Stream<MonthFundPlan> listAnaywary(Principal principal, Collection<String> corporationIds, Collection<String> currencyIds, int year, int month) {
        Operator operator = operatorRepository.find(principal.getName());
        Purpose purpose = purposeRepository.findAll()
                .findFirst()
                .orElseGet(() -> {
                    Purpose p = new Purpose();
                    p.setName("其他");
                    p._operator(operator);
                    return purposeRepository.save(p);
                });
        return J.emptyIfNull(corporationIds)
                .stream()
                .distinct()
                .map(corporationRepository::find)
                .flatMap(corporation -> J.emptyIfNull(currencyIds)
                        .stream()
                        .distinct()
                        .map(currencyRepository::find)
                        .map(currency -> {
                            MonthFundPlan monthFundPlan = new MonthFundPlan();
                            monthFundPlan.setCorporation(corporation);
                            monthFundPlan.setCurrency(currency);
                            monthFundPlan.setPurpose(purpose);
                            monthFundPlan.setYear(year);
                            monthFundPlan.setMonth(month);
                            monthFundPlan.setMoney(BigDecimal.ZERO);
                            monthFundPlan._operator(operator);
                            return monthFundPlanRepository.save(monthFundPlan);
                        })
                );
    }
}
