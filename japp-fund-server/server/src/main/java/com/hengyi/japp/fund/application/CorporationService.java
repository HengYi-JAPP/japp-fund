package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.CorporationImportCommand;
import com.hengyi.japp.fund.application.command.CorporationUpdateCommand;
import com.hengyi.japp.fund.domain.Corporation;

import java.security.Principal;

public interface CorporationService {
    Corporation create(Principal principal, CorporationImportCommand command) throws Exception;

    Corporation update(Principal principal, String id, CorporationUpdateCommand command) throws Exception;
}
