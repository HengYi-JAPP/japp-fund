package com.hengyi.japp.fund.application.command;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@Data
public class OperatorImportCommand implements Serializable {
    private String oaId;
    private String hrId;

}
