package com.hengyi.japp.fund.interfaces.fi.domain;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;
import org.jzb.J;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-12-4.
 * 资金系统中的日执行
 */
public class AccountFund implements Fundlike, Serializable {
    protected final BigDecimal money;
    private final Object id;
    private final Account account;
    private final Date date;
    private final BigDecimal amount;
    private final int direction;
    private final String oppaccountname;
    private final String abstractinfo;
    private final String remarkinfo;

    public AccountFund(Map<String, Object> map, Map<Object, Account> accountMap) {
        id = map.get("ID");
        direction = ((BigDecimal) map.get("DIRECTION")).intValue();
        date = (Date) map.get("TRANSACTIONTIME");
        amount = (BigDecimal) map.get("AMOUNT");
        oppaccountname = (String) map.get("OPPACCOUNTNAME");
        abstractinfo = (String) map.get("ABSTRACTINFO");
        remarkinfo = (String) map.get("REMARKINFO");
        account = accountMap.get(map.get("ACCOUNTID"));

        money = direction == 1 ? amount.multiply(BigDecimal.valueOf(-1)) : amount;
    }

    public AccountFund(AccountFund fund, BigDecimal money) {
        id = null;
        amount = null;
        abstractinfo = null;
        remarkinfo = null;
        direction = 0;
        oppaccountname = "合并";
        this.money = money;
        account = fund.account;
        date = fund.date;
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
    public BigDecimal getMoney() {
        return money;
    }

    @Override
    public String getNote() {
        return Stream.of(oppaccountname, abstractinfo, remarkinfo)
                .filter(J::nonBlank)
                .collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountFund that = (AccountFund) o;
        if (id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
