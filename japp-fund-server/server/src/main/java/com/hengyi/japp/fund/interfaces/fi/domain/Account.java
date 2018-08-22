package com.hengyi.japp.fund.interfaces.fi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jzb on 16-12-3.
 * 资金系统  银行账户
 */
public class Account implements Serializable, AuthService.AuthFilterable {
    private final Object id;
    private final SysAgency sysAgency;
    private final Currency currency;
    private final FundProperty fundProperty;
    private final Date closeDate;

    public Account(Map<String, Object> map, Map<String, Currency> currencyMap, Map<String, SysAgency> sysAgencyMap, Map<String, FundProperty> fundPropertyMap) {
        id = map.get("ID");
        closeDate = (Date) map.get("CLOSEDATE");
        this.currency = currencyMap.get(map.get("CURRENCYCODE"));
        this.sysAgency = sysAgencyMap.get(map.get("ID_BELONGORG"));
        this.fundProperty = fundPropertyMap.get(map.get("FUNDPROPERTYCODE"));
    }

    public Pair<Corporation, Currency> pair() {
        return Pair.of(getCorporation(), getCurrency());
    }

    @JsonIgnore
    public Object getId() {
        return id;
    }

    @Override
    public Corporation getCorporation() {
        return sysAgency.getCorporation();
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @JsonIgnore
    public FundProperty getFundProperty() {
        return fundProperty;
    }

    @JsonIgnore
    public Date getCloseDate() {
        return closeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account that = (Account) o;
        if (id == null || that.id == null) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
