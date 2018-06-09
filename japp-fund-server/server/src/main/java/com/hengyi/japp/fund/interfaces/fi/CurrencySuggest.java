package com.hengyi.japp.fund.interfaces.fi;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jzb on 16-11-16.
 */
public class CurrencySuggest implements Serializable {
    private String code;
    private String name;

    public CurrencySuggest(Map<String, Object> map) {
        code = (String) map.get("DICTCODE");
        name = (String) map.get("DICTNAME");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
