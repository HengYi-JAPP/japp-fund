package com.hengyi.japp.fund.application.command;

import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
public class OperatorImportCommand implements Serializable {
    private String oaId;
    private String hrId;

    public String getOaId() {
        return oaId;
    }

    public void setOaId(String oaId) {
        this.oaId = oaId;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }
}
