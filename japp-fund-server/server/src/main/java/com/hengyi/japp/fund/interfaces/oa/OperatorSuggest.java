package com.hengyi.japp.fund.interfaces.oa;

import java.io.Serializable;

/**
 * Created by jzb on 16-11-16.
 */
public class OperatorSuggest implements Serializable {
    private String oaId;
    private String hrId;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
