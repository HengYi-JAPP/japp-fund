package com.hengyi.japp.fund.infrastructure.messaging.jms;

import com.hengyi.japp.fund.domain.event.EventEntity;
import com.hengyi.japp.fund.domain.event.EventRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.MapMessage;

/**
 * Created by jzb on 16-11-3.
 */
@Stateless
public class EventService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private EventRepository eventRepository;

    public <T extends EventEntity> T save(Class<T> clazz, MapMessage msg) throws Exception {
        T t = clazz.newInstance();
        t.from(msg);
        return (T) eventRepository.save(t);
    }
}
