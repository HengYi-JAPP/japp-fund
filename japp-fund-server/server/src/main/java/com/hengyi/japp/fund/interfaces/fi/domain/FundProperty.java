package com.hengyi.japp.fund.interfaces.fi.domain;

import java.util.Map;
import java.util.Objects;

/**
 * Created by jzb on 16-12-3.
 */
public class FundProperty {
    private final String code;
    private final String name;

    public FundProperty(Map<String, Object> map) {
        code = (String) map.get("DICTCODE");
        name = (String) map.get("DICTNAME");
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FundProperty that = (FundProperty) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
