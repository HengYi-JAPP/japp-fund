package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：
 *
 * @author jzb 2018-01-24
 */
@Data
public class InitCdhpCommand implements Serializable {
    @NotNull
    private EntityDTO corporation;
    @NotNull
    private BigDecimal balance;

}
