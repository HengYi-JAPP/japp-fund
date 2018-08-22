package com.hengyi.japp.fund.application.command;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 16-11-16.
 */
@Data
public class CurrencyImportCommand implements Serializable {
    @NotBlank
    private String code;

}
