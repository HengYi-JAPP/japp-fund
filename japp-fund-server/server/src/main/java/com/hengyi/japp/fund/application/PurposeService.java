package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.PurposeUpdateCommand;
import com.hengyi.japp.fund.domain.Purpose;

import java.security.Principal;

public interface PurposeService {

    Purpose create(Principal principal, PurposeUpdateCommand command) throws Exception;

    Purpose update(Principal principal, String id, PurposeUpdateCommand command) throws Exception;
}
