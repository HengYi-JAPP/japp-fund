package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.PermissionDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jzb on 16-10-29.
 */
public class OperatorGroupUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1)
    private Collection<PermissionDTO> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
