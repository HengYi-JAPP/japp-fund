package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.CorporationService;
import com.hengyi.japp.fund.application.command.CorporationImportCommand;
import com.hengyi.japp.fund.application.command.CorporationUpdateCommand;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.interfaces.fi.CorporationSuggest;
import com.hengyi.japp.fund.interfaces.fi.FiService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class CorporationServiceImpl implements CorporationService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private AuthService authService;
    @Inject
    private FiService fiService;

    @Override
    public Corporation create(Principal principal, CorporationImportCommand command) throws Exception {
        CorporationSuggest suggest = fiService.findCorporationSuggest(command.getId());
        Corporation corporation = corporationRepository.findByCode(suggest.getCode());
        if (corporation == null) {
            corporation = new Corporation();
        }
        return save(principal, corporation, MAPPER.convertValue(suggest, CorporationUpdateCommand.class));
    }

    private Corporation save(Principal principal, Corporation o, CorporationUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        o.setCode(command.getCode());
        o.setName(command.getName());
        o.setSortBy(command.getSortBy());
        o.setDeleted(false);
        Operator operator = operatorRepository.find(principal.getName());
        o._operator(operator);
        return corporationRepository.save(o);
    }

    @Override
    public Corporation update(Principal principal, String id, CorporationUpdateCommand command) throws Exception {
        Corporation oldCorporation = corporationRepository.find(id);
        return save(principal, oldCorporation, command);
    }

}
