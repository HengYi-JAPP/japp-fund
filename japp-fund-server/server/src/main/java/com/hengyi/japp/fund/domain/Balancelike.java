package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.application.AuthService;
import org.apache.commons.lang3.tuple.Triple;
import org.jzb.J;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public interface Balancelike extends AuthService.AuthFilterable {

    default Triple<Corporation, Currency, LocalDate> triple() {
        return Triple.of(getCorporation(), getCurrency(), localDate());
    }

    default LocalDate localDate() {
        return J.localDate(getDate());
    }

    Corporation getCorporation();

    Currency getCurrency();

    Date getDate();

    BigDecimal getBalance();
}
