package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaCorporationRepository extends JpaCURDRepository<Corporation, String> implements CorporationRepository, Serializable {
    @Override
    public Corporation findByCode(String code) {
        TypedQuery<Corporation> query = em.createNamedQuery("Corporation.findByCode", Corporation.class)
                .setParameter("code", code)
                .setMaxResults(1);
        return Util.getSingle(query);
    }
}
