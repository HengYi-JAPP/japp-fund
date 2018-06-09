package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.share.CURDEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_MONTHFUNDPLAN")
@NamedQueries({
        @NamedQuery(name = "MonthFundPlan.queryByCorpCurYear", query = "SELECT o FROM MonthFundPlan o WHERE o.corporation IN :corporations AND o.currency IN :currencies AND o.year=:year AND o.deleted=FALSE"),
        @NamedQuery(name = "MonthFundPlan.queryByCorpCurYearMonth", query = "SELECT o FROM MonthFundPlan o WHERE o.corporation IN :corporations AND o.currency IN :currencies AND o.year=:year AND o.month=:month AND o.deleted=FALSE"),
})
public class MonthFundPlan extends AbstractLoggableEntity implements CURDEntity<String>, AuthService.AuthFilterable {
    @NotNull
    @ManyToOne
    private Corporation corporation;
    @NotNull
    @ManyToOne
    private Currency currency;
    @Min(2016)
    private int year;
    @Min(1)
    @Max(12)
    private int month;
    @NotNull
    @ManyToOne
    private Purpose purpose;
    @NotNull
    private BigDecimal money;
    private String note;

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

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
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
}
