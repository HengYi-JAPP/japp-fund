package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.share.CURDEntity;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jzb on 16-10-20.
 */
@MappedSuperclass
public abstract class AbstractFundlikeEntity extends AbstractLoggableEntity implements Fundlike, CURDEntity<String> {
    @NotNull
    @ManyToOne
    protected MonthFundPlan monthFundPlan;
    @NotNull
    @ManyToOne
    protected Corporation corporation;
    @NotNull
    @ManyToOne
    protected Currency currency;
    @NotNull
    @Temporal(TemporalType.DATE)
    protected Date date;
    @NotNull
    @ManyToOne
    protected Purpose purpose;
    @NotNull
    protected BigDecimal money;
    protected String note;

    public MonthFundPlan getMonthFundPlan() {
        return monthFundPlan;
    }

    public void setMonthFundPlan(MonthFundPlan monthFundPlan) {
        this.monthFundPlan = monthFundPlan;
    }

    @Override
    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
