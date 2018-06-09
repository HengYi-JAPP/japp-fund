package com.hengyi.japp.fund.interfaces.fi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by jzb on 16-11-16.
 */
public class CorporationSuggest implements Serializable {
    private long id;
    private String code;
    private String name;

    public CorporationSuggest(Map<String, Object> map) {
        id = ((BigDecimal) map.get("ID")).longValue();
        code = (String) map.get("AGENCYCODE");
        name = (String) map.get("AGENCYNAME");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
