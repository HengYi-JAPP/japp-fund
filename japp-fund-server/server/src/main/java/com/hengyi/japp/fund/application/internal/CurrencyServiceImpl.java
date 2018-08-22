package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.CurrencyService;
import com.hengyi.japp.fund.application.command.CurrencyImportCommand;
import com.hengyi.japp.fund.application.command.CurrencyUpdateCommand;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.interfaces.fi.CurrencySuggest;
import com.hengyi.japp.fund.interfaces.fi.FiService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

import static com.github.ixtf.japp.core.Constant.MAPPER;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class CurrencyServiceImpl implements CurrencyService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private AuthService authService;
    @Inject
    private FiService fiService;

    @Override
    public Currency create(Principal principal, CurrencyImportCommand command) throws Exception {
        CurrencySuggest suggest = fiService.findCurrencySuggest(command.getCode());
        Currency currency = currencyRepository.findByCode(suggest.getCode());
        if (currency == null) {
            currency = new Currency();
        }
        return save(principal, currency, MAPPER.convertValue(suggest, CurrencyUpdateCommand.class));
    }

    private Currency save(Principal principal, Currency o, CurrencyUpdateCommand command) throws Exception {
        authService.checkAdmin(principal);
        o.setCode(command.getCode());
        o.setName(command.getName());
        o.setSortBy(command.getSortBy());
        o.setDeleted(false);
        Operator operator = operatorRepository.find(principal.getName());
        o._operator(operator);
        return currencyRepository.save(o);
    }

    @Override
    public Currency update(Principal principal, String id, CurrencyUpdateCommand command) throws Exception {
        Currency oldCurrency = currencyRepository.find(id);
        return save(principal, oldCurrency, command);
    }

}
