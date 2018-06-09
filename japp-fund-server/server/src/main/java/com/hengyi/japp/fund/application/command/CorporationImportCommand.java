package com.hengyi.japp.fund.application.command;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by jzb on 16-11-16.
 */
public class CorporationImportCommand implements Serializable {
    @Min(1)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
