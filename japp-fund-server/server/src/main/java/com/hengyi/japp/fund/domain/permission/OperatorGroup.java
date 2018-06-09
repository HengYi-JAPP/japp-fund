package com.hengyi.japp.fund.domain.permission;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hengyi.japp.fund.domain.AbstractLoggableEntity;
import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by jzb on 16-10-28.
 */
@Entity
@Table(name = "T_OPERATORGROUP")
public class OperatorGroup extends AbstractLoggableEntity implements CURDEntity<String> {
    @NotBlank
    private String name;
    @JsonIgnore
    @XmlTransient
    @Lob
    private String permissionJsonString;

    @JsonGetter
    public Collection<Permission> getPermissions() {
        return Permission.fromJson(permissionJsonString);
    }

    public void setPermissions(Collection<Permission> permissions) throws IOException {
        permissionJsonString = Permission.toString(permissions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionJsonString() {
        return permissionJsonString;
    }

    public void setPermissionJsonString(String permissionJsonString) {
        this.permissionJsonString = permissionJsonString;
    }
}
