package com.hengyi.japp.fund.application.dto;

import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.permission.RoleType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created by jzb on 16-11-19.
 */
@Data
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

}
