package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jzb on 16-10-26.
 */
@Data
public class MonthFundPlanUpdateCommand implements Serializable {
    @Min(2016)
    private int year;
    @Min(1)
    @Max(12)
    private int month;
    @NotNull
    private EntityDTO corporation;
    @NotNull
    private EntityDTO currency;
    @NotNull
    private EntityDTO purpose;
    @NotNull
    private BigDecimal money;
    private String note;

}
