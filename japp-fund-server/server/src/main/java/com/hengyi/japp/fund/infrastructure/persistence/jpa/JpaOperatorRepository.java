package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import org.jzb.J;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.security.Principal;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaOperatorRepository extends JpaCURDRepository<Operator, String> implements OperatorRepository, Serializable {
    @Override
    public Operator findBy(Principal principal) {
        return find(principal.getName());
    }

    @Override
    public Operator findByCas(String casPrincipal, String oaId) {
        Operator result = find(casPrincipal);
        if (result != null)
            return result;
        result = findByHrIdOrOaId(casPrincipal);
        if (result != null)
            return result;
        return J.isBlank(oaId) ? null : findByHrIdOrOaId(oaId);
    }

    @Override
    public Operator findByHrIdOrOaId(String id) {
        Operator result = findByHrId(id);
        if (result != null)
            return result;
        result = findByOaId(id);
        if (result != null)
            return result;
        return null;
    }

    private Operator findByHrId(String casPrincipal) {
        TypedQuery<Operator> typedQuery = em.createNamedQuery("Operator.findByHrId", Operator.class)
                .setParameter("hrId", casPrincipal)
                .setMaxResults(1);
        return Util.getSingle(typedQuery);
    }

    private Operator findByOaId(String casPrincipal) {
        TypedQuery<Operator> typedQuery = em.createNamedQuery("Operator.findByOaId", Operator.class)
                .setParameter("oaId", casPrincipal)
                .setMaxResults(1);
        return Util.getSingle(typedQuery);
    }

}
