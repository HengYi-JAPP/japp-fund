package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "T_CORPORATION")
@NamedQueries({
        @NamedQuery(name = "Corporation.findByCode", query = "SELECT o FROM Corporation o WHERE o.code=:code"),
})
public class BalanceSumType extends AbstractLoggableEntity implements CURDEntity<String>, Comparable<BalanceSumType> {
    @NotBlank
    @Column(length = 100)
    private String name;
    private int sortBy;

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
    public int compareTo(@NotNull BalanceSumType o) {
        return o.sortBy - this.sortBy;
    }
}
