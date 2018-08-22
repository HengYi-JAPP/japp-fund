package com.hengyi.japp.fund.application.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class EntityDTO implements Serializable {
    @NotNull
    private String id;

}
