package com.hengyi.japp.fund.application.internal;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.application.AdminService;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.command.AppClientUpdateCommand;
import com.hengyi.japp.fund.application.command.InitCdhpCommand;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.repository.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

import static com.hengyi.japp.fund.Constant.CDHP;
import static com.hengyi.japp.fund.infrastructure.persistence.jpa.JpaFundBalanceRepository.INIT_LD;

/**
 * Created by jzb on 16-10-28.
 */
@Stateless
public class AdminServiceImpl implements AdminService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private FundBalanceRepository fundBalanceRepository;
    @Inject
    private AppClientRepository appClientRepository;
    @Inject
    private AuthService authService;

    @Override
    public AppClient createAppClient(Principal principal, AppClientUpdateCommand command) throws Exception {
        return save(principal, new AppClient(), command);
    }

    private AppClient save(Principal principal, AppClient o, AppClientUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        o.setAppId(command.getAppId());
        o.setName(command.getName());
        o.setAppSecret(command.getAppSecret());
        Operator operator = operatorRepository.find(principal.getName());
        o._operator(operator);
        return appClientRepository.save(o);
    }

    @Override
    public AppClient updateAppClient(Principal principal, String id, AppClientUpdateCommand command) throws Exception {
        AppClient appClient = appClientRepository.find(id);
        return save(principal, appClient, command);
    }

    @Override
    public void deleteAppClient(Principal principal, String id) throws Exception {
        authService.checkAdmin(principal);
        appClientRepository.delete(id);
    }

    @Override
    public FundBalance initCdhp(InitCdhpCommand command) {
        final Corporation corporation = corporationRepository.find(command.getCorporation().getId());
        final Currency currency = currencyRepository.findByCode(CDHP);
        FundBalance fundBalance = new FundBalance();
        fundBalance.setDate(J.date(INIT_LD));
        fundBalance.setCorporation(corporation);
        fundBalance.setCurrency(currency);
        fundBalance.setBalance(command.getBalance());
        return fundBalanceRepository.save(fundBalance);
    }

}
