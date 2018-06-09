package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.AppClientUpdateCommand;
import com.hengyi.japp.fund.application.command.InitCdhpCommand;
import com.hengyi.japp.fund.domain.AppClient;
import com.hengyi.japp.fund.domain.FundBalance;

import java.security.Principal;

/**
 * Created by jzb on 16-10-28.
 */
public interface AdminService {
    AppClient createAppClient(Principal principal, AppClientUpdateCommand command) throws Exception;

    AppClient updateAppClient(Principal principal, String id, AppClientUpdateCommand command) throws Exception;

    void deleteAppClient(Principal principal, String id) throws Exception;

    FundBalance initCdhp(InitCdhpCommand command);
}
