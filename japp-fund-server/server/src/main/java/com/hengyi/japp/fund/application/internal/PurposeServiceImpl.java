package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.PurposeService;
import com.hengyi.japp.fund.application.command.PurposeUpdateCommand;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.Purpose;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.domain.repository.PurposeRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class PurposeServiceImpl implements PurposeService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private PurposeRepository purposeRepository;
    @Inject
    private AuthService authService;

    @Override
    public Purpose create(Principal principal, PurposeUpdateCommand command) throws Exception {
        return save(principal, new Purpose(), command);
    }

    protected Purpose save(Principal principal, Purpose purpose, PurposeUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        purpose.setName(command.getName());
        purpose.setSortBy(command.getSortBy());
        Operator operator = operatorRepository.find(principal.getName());
        purpose._operator(operator);
        return purposeRepository.save(purpose);
    }

    @Override
    public Purpose update(Principal principal, String id, PurposeUpdateCommand command) throws Exception {
        return save(principal, purposeRepository.find(id), command);
    }

}
