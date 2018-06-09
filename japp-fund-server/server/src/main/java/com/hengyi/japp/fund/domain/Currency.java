package com.hengyi.japp.fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.hengyi.japp.fund.Constant.CDHP;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_CURRENCY")
@NamedQueries({
        @NamedQuery(name = "Currency.findByCode", query = "SELECT o FROM Currency o WHERE o.code=:code"),
})
public class Currency extends AbstractLoggableEntity implements CURDEntity<String>, Comparable<Currency> {
    @NotBlank
    @Column(length = 20)
    private String code;
    @NotBlank
    @Column(length = 50)
    private String name;
    private int sortBy;

    @JsonIgnore
    public boolean isCdhp() {
        return Objects.equals(CDHP, code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public int compareTo(@NotNull Currency o) {
        return o.sortBy - this.sortBy;
    }

    @Override
    public String toString() {
        return name;
    }
}
