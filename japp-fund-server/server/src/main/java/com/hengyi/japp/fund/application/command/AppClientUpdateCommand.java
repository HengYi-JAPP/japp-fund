package com.hengyi.japp.fund.application.command;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 16-10-28.
 */
@Data
public class AppClientUpdateCommand implements Serializable {
    @NotBlank
    private String appId;
    @NotBlank
    private String name;
    private String appSecret;

}
