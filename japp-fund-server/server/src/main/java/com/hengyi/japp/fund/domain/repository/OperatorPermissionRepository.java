package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.share.CURDEntityRepository;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by jzb on 16-10-28.
 */
public interface OperatorPermissionRepository extends CURDEntityRepository<OperatorPermission, String> {
    OperatorPermission save(Operator operator, OperatorPermission operatorPermission);

    Collection<Permission> getPermissions(Principal principal);

//    Collection<OperatorGroup> findAllOperatorGroup(Collection<EntityDTO> operatorGroups);

}
