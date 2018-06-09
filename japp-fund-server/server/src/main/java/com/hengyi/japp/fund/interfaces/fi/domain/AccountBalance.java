package com.hengyi.japp.fund.interfaces.fi.domain;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jzb on 16-12-3.
 * 银行账户余额
 */
public class AccountBalance implements Serializable, Balancelike {
    private final Object id;
    private final Account account;
    private final Date date;
    private final BigDecimal balance;

    public AccountBalance(Map<String, Object> map, Map<Object, Account> accountMap) {
        id = map.get("ID");
        date = (Date) map.get("BALANCEDATE");
        balance = (BigDecimal) map.get("BALANCE");
        account = accountMap.get(map.get("ACCOUNTID"));
    }

    @Override
    public Corporation getCorporation() {
        return account.getCorporation();
    }

    @Override
    public Currency getCurrency() {
        return account.getCurrency();
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBalance that = (AccountBalance) o;
        if (id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
