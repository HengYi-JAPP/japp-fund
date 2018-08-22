package com.hengyi.japp.fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ixtf.japp.codec.Jcodec;
import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.share.CURDEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by jzb on 17-7-13.
 */
@Entity
@Table(name = "T_FUNDBALANCE")
@NamedQueries({
        @NamedQuery(name = "FundBalance.findMaxNearByCorpCurDate", query = "SELECT o FROM FundBalance o WHERE o.corporation=:corporation AND o.currency=:currency AND o.date=(SELECT MAX(sub.date) FROM FundBalance sub WHERE sub.corporation=:corporation AND sub.currency=:currency AND sub.date<=:date AND sub.deleted=FALSE) AND o.deleted=FALSE"),
        @NamedQuery(name = "FundBalance.queryByCorpCurDate", query = "SELECT o FROM FundBalance o WHERE o.corporation IN :corporations AND o.currency IN :currencies AND o.date BETWEEN :sDate AND :eDate AND o.deleted=FALSE"),
        @NamedQuery(name = "FundBalance.addBalance", query = "UPDATE FundBalance o SET o.balance=o.balance+(:money) WHERE o.corporation=:corporation AND o.currency=:currency AND o.date>=:date AND o.deleted=FALSE"),
})
public class FundBalance extends AbstractEntity implements CURDEntity<String>, Comparable<FundBalance>, Balancelike {
    @NotNull
    @ManyToOne
    private Corporation corporation;
    @NotNull
    @ManyToOne
    private Currency currency;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;
    @NotNull
    private BigDecimal balance;

    public FundBalance() {
    }

    public FundBalance(Corporation corporation, Currency currency, LocalDate date, BigDecimal balance) {
        this.corporation = corporation;
        this.currency = currency;
        this.date = J.date(date);
        this.balance = balance;
    }

    public static String generateId(Corporation corporation, Currency currency, Date date) {
        String corporationId = corporation.getId();
        String currencyId = currency.getId();
        String dateS = J.localDate(date).toString();
        return Jcodec.uuid58(corporationId, currencyId, dateS);
    }

    @JsonIgnore
    public LocalDate getLd() {
        return J.localDate(date);
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

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int compareTo(@NotNull FundBalance o) {
        return (int) (this.date.getTime() - o.getDate().getTime());
    }
}
