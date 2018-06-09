package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 描述：
 *
 * @author jzb 2018-01-24
 */
public class InitCdhpCommand {
    @NotNull
    private EntityDTO corporation;
    @NotNull
    private BigDecimal balance;

    public EntityDTO getCorporation() {
        return corporation;
    }

    public void setCorporation(EntityDTO corporation) {
        this.corporation = corporation;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
