package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.OperatorGroupService;
import com.hengyi.japp.fund.application.command.OperatorGroupUpdateCommand;
import com.hengyi.japp.fund.application.dto.PermissionDTO;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.domain.permission.OperatorGroup;
import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.repository.OperatorGroupRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.exception.PermissionInputException;
import org.jzb.J;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.jzb.Constant.ErrorCode.SYSTEM;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class OperatorGroupServiceImpl implements OperatorGroupService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private OperatorGroupRepository operatorGroupRepository;
    @Inject
    private AuthService authService;
    @Inject
    private ApplicationEvents applicationEvents;

    @Override
    public OperatorGroup create(Principal principal, OperatorGroupUpdateCommand command) throws Exception {
        OperatorGroup operatorGroup = save(principal, new OperatorGroup(), command);
        applicationEvents.curdEvent(principal, OperatorGroup.class, operatorGroup.getId(), EventType.CREATE, command);
        return operatorGroup;
    }

    private OperatorGroup save(Principal principal, OperatorGroup operatorGroup, OperatorGroupUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        operatorGroup.setName(command.getName());
        if (J.isEmpty(command.getPermissions())) {
            throw new PermissionInputException(SYSTEM, null);
        }
        Collection<Permission> permissions = command.getPermissions().stream()
                .map(PermissionDTO::toPermission)
                .collect(Collectors.toList());
        operatorGroup.setPermissions(permissions);
        Operator operator = operatorRepository.find(principal.getName());
        operatorGroup._operator(operator);
        return operatorGroupRepository.save(operatorGroup);
    }

    @Override
    public OperatorGroup update(Principal principal, String id, OperatorGroupUpdateCommand command) throws Exception {
        OperatorGroup operatorGroup = save(principal, operatorGroupRepository.find(id), command);
        applicationEvents.curdEvent(principal, OperatorGroup.class, id, EventType.UPDATE, command);
        return operatorGroup;
    }

}
