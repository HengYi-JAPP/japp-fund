package com.hengyi.japp.fund.application.command;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@Data
public class CurrencyUpdateCommand implements Serializable {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private int sortBy;

}
