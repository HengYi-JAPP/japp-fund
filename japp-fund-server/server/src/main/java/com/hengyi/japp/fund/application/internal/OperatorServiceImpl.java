package com.hengyi.japp.fund.application.internal;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.OperatorService;
import com.hengyi.japp.fund.application.command.OperatorImportCommand;
import com.hengyi.japp.fund.application.command.OperatorUpdateCommand;
import com.hengyi.japp.fund.application.dto.EntityDTO;
import com.hengyi.japp.fund.application.dto.PermissionDTO;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.domain.permission.OperatorGroup;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.repository.OperatorGroupRepository;
import com.hengyi.japp.fund.domain.repository.OperatorPermissionRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.interfaces.oa.OaService;
import com.hengyi.japp.fund.interfaces.oa.OperatorSuggest;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

public class OperatorServiceImpl implements OperatorService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private OperatorGroupRepository operatorGroupRepository;
    @Inject
    private OperatorPermissionRepository operatorPermissionRepository;
    @Inject
    private AuthService authService;
    @Inject
    private OaService oaService;
    @Inject
    private ApplicationEvents applicationEvents;

    @Override
    public Operator create(Principal principal, OperatorImportCommand command) throws Exception {
        authService.checkAdmin(principal);
        String hrId = command.getHrId();
        String oaId = command.getOaId();
        if (J.isBlank(hrId) && J.isBlank(oaId)) {
            throw new RuntimeException();
        }

        Operator operator = null;
        if (J.nonBlank(hrId)) {
            operator = operatorRepository.findByHrIdOrOaId(hrId);
        }
        if (operator == null) {
            operator = operatorRepository.findByHrIdOrOaId(oaId);
        }
        if (operator != null) {
            operator.setDeleted(false);
            return operatorRepository.save(operator);
        }

        OperatorSuggest operatorSuggest = oaService.findOperatorSuggest(command);
        operator = new Operator();
        operator.setHrId(operatorSuggest.getHrId());
        operator.setOaId(operatorSuggest.getOaId());
        operator.setName(operatorSuggest.getName());
        return operatorRepository.save(operator);
    }

    @Override
    public OperatorPermission update(Principal principal, String id, OperatorUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        Operator operator = operatorRepository.find(id);
        OperatorPermission operatorPermission = operatorPermissionRepository.find(id);
        if (operatorPermission == null) {
            operatorPermission = new OperatorPermission();
        }

        Collection<OperatorGroup> operatorGroups = J.emptyIfNull(command.getOperatorGroups())
                .stream()
                .map(EntityDTO::getId)
                .map(operatorGroupRepository::find)
                .collect(Collectors.toSet());
        operatorPermission.setOperatorGroups(operatorGroups);

        Collection<Permission> permissions = J.emptyIfNull(command.getPermissions())
                .stream()
                .map(PermissionDTO::toPermission)
                .collect(Collectors.toSet());
        operatorPermission.setPermissions(permissions);

        operatorPermission = operatorPermissionRepository.save(operator, operatorPermission);
        applicationEvents.curdEvent(principal, OperatorPermission.class, operatorPermission.getId(), EventType.UPDATE, command);

        operator.setAdmin(command.getAdmin());
        operator.setName(command.getName());
        operatorRepository.save(operator);

        return operatorPermission;
    }

}
