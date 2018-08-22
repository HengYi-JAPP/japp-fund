package com.hengyi.japp.fund.application.command;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jzb on 16-10-26.
 */
@Data
public class FundlikeUpdateCommand implements Serializable {
    @NotNull
    private Date date;
    @NotNull
    private BigDecimal money;
    private String note;

}
