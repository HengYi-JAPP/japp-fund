package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.share.CURDEntityRepository;

import java.security.Principal;

/**
 * Created by jzb on 16-10-20.
 */
public interface OperatorRepository extends CURDEntityRepository<Operator, String> {
    Operator findBy(Principal principal);

    Operator findByCas(String casPrincipal, String oaId);

    Operator findByHrIdOrOaId(String id);
}
