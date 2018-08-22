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
public class BatchFundlikeUpdateCommand implements Serializable {
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private BigDecimal money;
    private String note;

}
