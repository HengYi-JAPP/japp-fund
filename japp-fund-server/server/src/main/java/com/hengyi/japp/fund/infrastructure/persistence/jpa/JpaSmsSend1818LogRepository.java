package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.github.ixtf.japp.codec.Jcodec;
import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.domain.log.SmsSend1818Log;
import com.hengyi.japp.fund.domain.repository.SmsSend1818LogRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by jzb on 16-10-28.
 */
@ApplicationScoped
public class JpaSmsSend1818LogRepository implements SmsSend1818LogRepository {
    @Inject
    protected EntityManager em;

    @Override
    public SmsSend1818Log save(SmsSend1818Log smsSend1818Log) {
        if (J.isBlank(smsSend1818Log.getId())) {
            smsSend1818Log.setId(Jcodec.uuid58());
        }
        return em.merge(smsSend1818Log);
    }

    @Override
    public SmsSend1818Log find(String id) {
        return em.find(SmsSend1818Log.class, id);
    }
}
