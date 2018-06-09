package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-29.
 */
public class CorporationCurrenciesUpdateCommand implements Serializable {
    @NotNull
    @Size(min = 1)
    private List<EntityDTO> currencies;

    public List<EntityDTO> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<EntityDTO> currencies) {
        this.currencies = currencies;
    }
}
