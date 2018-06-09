package com.hengyi.japp.fund.domain.permission;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.hengyi.japp.fund.domain.AbstractEntity;
import com.hengyi.japp.fund.share.CURDEntity;
import org.jzb.J;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by jzb on 16-10-26.
 */
@Entity
@Table(name = "T_OPERATORPERMISSION")
@NamedQueries({
        @NamedQuery(name = "OperatorPermission.findByOperatorGroup", query = "SELECT o FROM OperatorPermission o WHERE :operatorGroup MEMBER OF o.operatorGroups"),
})
public class OperatorPermission extends AbstractEntity implements CURDEntity<String> {
    @ManyToMany
    @JoinTable(name = "T_OPERATORGROUP_T_OPERATOR")
    private Collection<OperatorGroup> operatorGroups;
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

    public void remove(OperatorGroup operatorGroup) {
        if (J.nonEmpty(operatorGroups)) {
            operatorGroups.remove(operatorGroup);
        }
    }

    public void add(OperatorGroup operatorGroup) {
        if (operatorGroups == null) {
            operatorGroups = Sets.newHashSet(operatorGroup);
        } else {
            operatorGroups.add(operatorGroup);
        }
    }

    public Collection<OperatorGroup> getOperatorGroups() {
        return operatorGroups;
    }

    public void setOperatorGroups(Collection<OperatorGroup> operatorGroups) {
        this.operatorGroups = operatorGroups;
    }

    public String getPermissionJsonString() {
        return permissionJsonString;
    }

    public void setPermissionJsonString(String permissionJsonString) {
        this.permissionJsonString = permissionJsonString;
    }
}
