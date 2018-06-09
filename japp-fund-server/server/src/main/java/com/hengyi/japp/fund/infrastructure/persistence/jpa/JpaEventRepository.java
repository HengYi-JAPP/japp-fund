package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.event.EventEntity;
import com.hengyi.japp.fund.domain.event.EventRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-28.
 */
@ApplicationScoped
public class JpaEventRepository implements EventRepository, Serializable {
    @PersistenceContext
    private EntityManager em;

    @Override
    public EventEntity save(EventEntity event) throws Exception {
        event.uuid();
        return em.merge(event);
    }
}
