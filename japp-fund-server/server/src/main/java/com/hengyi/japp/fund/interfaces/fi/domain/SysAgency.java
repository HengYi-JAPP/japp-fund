package com.hengyi.japp.fund.interfaces.fi.domain;

import com.hengyi.japp.fund.domain.Corporation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jzb on 16-12-3.
 * 资金系统中的机构  概念可以理解为 公司
 */
public class SysAgency implements Serializable {
    private final String id;
    private final String code;
    private final Corporation corporation;

    public SysAgency(Map<String, Object> map, Map<String, Corporation> corporationMap) {
        id = map.get("ID").toString();
        code = map.get("AGENCYCODE").toString();
        this.corporation = corporationMap.get(code);
    }

    public String getId() {
        return id;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysAgency that = (SysAgency) o;
        if (id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
