package com.hengyi.japp.fund.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
public class CorporationUpdateCommand implements Serializable {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private int sortBy;

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
}
