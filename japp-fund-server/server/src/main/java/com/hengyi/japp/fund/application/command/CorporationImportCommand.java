package com.hengyi.japp.fund.application.command;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by jzb on 16-11-16.
 */
@Data
public class CorporationImportCommand implements Serializable {
    @Min(1)
    private long id;

}
