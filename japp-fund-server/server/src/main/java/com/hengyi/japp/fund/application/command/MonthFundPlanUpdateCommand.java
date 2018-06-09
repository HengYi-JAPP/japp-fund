package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jzb on 16-10-26.
 */
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public EntityDTO getCorporation() {
        return corporation;
    }

    public void setCorporation(EntityDTO corporation) {
        this.corporation = corporation;
    }

    public EntityDTO getCurrency() {
        return currency;
    }

    public void setCurrency(EntityDTO currency) {
        this.currency = currency;
    }

    public EntityDTO getPurpose() {
        return purpose;
    }

    public void setPurpose(EntityDTO purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
