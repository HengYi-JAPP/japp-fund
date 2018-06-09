package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.CurrencyImportCommand;
import com.hengyi.japp.fund.application.command.CurrencyUpdateCommand;
import com.hengyi.japp.fund.domain.Currency;

import java.security.Principal;

public interface CurrencyService {
    Currency create(Principal principal, CurrencyImportCommand command) throws Exception;

    Currency update(Principal principal, String id, CurrencyUpdateCommand command) throws Exception;
}
