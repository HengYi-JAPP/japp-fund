package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;
import com.hengyi.japp.fund.application.dto.PermissionDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jzb on 16-10-20.
 */
public class OperatorUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    private Boolean admin = false;
    private Collection<EntityDTO> operatorGroups;
    private Collection<PermissionDTO> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Collection<EntityDTO> getOperatorGroups() {
        return operatorGroups;
    }

    public void setOperatorGroups(Collection<EntityDTO> operatorGroups) {
        this.operatorGroups = operatorGroups;
    }

    public Collection<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
