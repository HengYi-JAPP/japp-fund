package com.hengyi.japp.fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_CORPORATION")
@NamedQueries({
        @NamedQuery(name = "Corporation.findByCode", query = "SELECT o FROM Corporation o WHERE o.code=:code"),
})
public class Corporation extends AbstractLoggableEntity implements CURDEntity<String>, Comparable<Corporation> {
    @NotBlank
    @Column(length = 20)
    private String code;
    @NotBlank
    @Column(length = 100)
    private String name;
    private int sortBy;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_CORPORATION_T_CURRENCY")
    private Collection<Currency> currencies;

    public Collection<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Collection<Currency> currencies) {
        this.currencies = currencies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public int compareTo(@NotNull Corporation o) {
        return o.sortBy - this.sortBy;
    }

    @Override
    public String toString() {
        return name;
    }
}
