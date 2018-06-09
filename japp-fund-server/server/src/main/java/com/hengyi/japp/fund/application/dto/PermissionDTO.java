package com.hengyi.japp.fund.application.dto;

import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.permission.RoleType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created by jzb on 16-11-19.
 */
public class PermissionDTO implements Serializable {
    private boolean allCorporation;
    private Set<String> corporationIds;
    private boolean allCurrency;
    private Set<String> currencyIds;
    @NotNull
    @Size(min = 1)
    private Collection<RoleType> roleTypes;

    public Permission toPermission() {
        Permission permission = new Permission();
        permission.setRoleTypes(roleTypes);

        Collection<String> corporationIds = allCorporation ? Permission.ALL : this.corporationIds;
        permission.setCorporationIds(corporationIds);

        Collection<String> currencyIds = allCurrency ? Permission.ALL : this.currencyIds;
        permission.setCurrencyIds(currencyIds);
        return permission;
    }

    public boolean isAllCorporation() {
        return allCorporation;
    }

    public void setAllCorporation(boolean allCorporation) {
        this.allCorporation = allCorporation;
    }

    public boolean isAllCurrency() {
        return allCurrency;
    }

    public void setAllCurrency(boolean allCurrency) {
        this.allCurrency = allCurrency;
    }

    public Set<String> getCorporationIds() {
        return corporationIds;
    }

    public void setCorporationIds(Set<String> corporationIds) {
        this.corporationIds = corporationIds;
    }

    public Set<String> getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(Set<String> currencyIds) {
        this.currencyIds = currencyIds;
    }

    public Collection<RoleType> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(Collection<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
    }

}
