package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-29.
 */
@Data
public class CorporationCurrenciesUpdateCommand implements Serializable {
    @NotNull
    @Size(min = 1)
    private List<EntityDTO> currencies;

}
