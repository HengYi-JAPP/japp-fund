package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.OperatorGroupUpdateCommand;
import com.hengyi.japp.fund.domain.permission.OperatorGroup;

import java.security.Principal;

public interface OperatorGroupService {
    OperatorGroup create(Principal principal, OperatorGroupUpdateCommand command) throws Exception;

    OperatorGroup update(Principal principal, String id, OperatorGroupUpdateCommand command) throws Exception;
}
