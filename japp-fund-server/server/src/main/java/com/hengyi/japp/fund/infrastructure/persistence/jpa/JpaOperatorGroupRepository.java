package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.permission.OperatorGroup;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.repository.OperatorGroupRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaOperatorGroupRepository extends JpaCURDRepository<OperatorGroup, String> implements OperatorGroupRepository, Serializable {
    @Override
    public void delete(String id) {
        OperatorGroup operatorGroup = find(id);
        em.createNamedQuery("OperatorPermission.findByOperatorGroup", OperatorPermission.class)
                .setParameter("operatorGroup", operatorGroup)
                .getResultList().stream()
                .forEach(operatorPermission -> {
                    operatorPermission.remove(operatorGroup);
                    em.merge(operatorPermission);
                });
        em.remove(operatorGroup);
    }
}
