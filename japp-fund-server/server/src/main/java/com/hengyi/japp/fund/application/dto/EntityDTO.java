package com.hengyi.japp.fund.application.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class EntityDTO implements Serializable {
    @NotNull
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
