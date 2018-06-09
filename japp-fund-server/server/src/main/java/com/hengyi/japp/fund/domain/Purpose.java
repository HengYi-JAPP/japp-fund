package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_PURPOSE")
public class Purpose extends AbstractLoggableEntity implements CURDEntity<String> {
    @NotBlank
    @Column(length = 200)
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
}
