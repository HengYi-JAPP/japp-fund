package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.OperatorImportCommand;
import com.hengyi.japp.fund.application.command.OperatorUpdateCommand;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;

import java.security.Principal;

public interface OperatorService {
    Operator create(Principal principal, OperatorImportCommand command) throws Exception;

    OperatorPermission update(Principal principal, String id, OperatorUpdateCommand command) throws Exception;
}
