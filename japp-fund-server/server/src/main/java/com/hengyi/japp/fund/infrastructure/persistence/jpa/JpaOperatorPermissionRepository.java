package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.google.common.collect.Lists;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.permission.OperatorGroup;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.repository.OperatorPermissionRepository;
import org.jzb.J;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaOperatorPermissionRepository extends JpaCURDRepository<OperatorPermission, String> implements OperatorPermissionRepository, Serializable {
    @Override
    public OperatorPermission save(Operator operator, OperatorPermission operatorPermission) {
        if (operatorPermission.isNew()) {
            operatorPermission.setId(operator.getId());
        }
        if (Objects.equals(operator.getId(), operatorPermission.getId()))
            return em.merge(operatorPermission);
        throw new IllegalAccessError();
    }

    @Override
    public OperatorPermission save(OperatorPermission entity) {
        throw new IllegalAccessError();
    }


    @Override
    public Collection<Permission> getPermissions(Principal principal) {
        OperatorPermission operatorPermission = find(principal.getName());
        Collection<Permission> result = Lists.newArrayList();
        Collection<OperatorGroup> operatorGroups = operatorPermission.getOperatorGroups();
        if (J.nonEmpty(operatorGroups)) {
            operatorGroups.forEach(operatorGroup -> result.addAll(operatorGroup.getPermissions()));
        }
        Collection<Permission> permissions = operatorPermission.getPermissions();
        if (J.nonEmpty(permissions)) {
            result.addAll(permissions);
        }
        return result;
    }

}
