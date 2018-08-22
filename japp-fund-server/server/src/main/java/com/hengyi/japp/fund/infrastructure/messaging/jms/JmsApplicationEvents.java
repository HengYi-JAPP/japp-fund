package com.hengyi.japp.fund.infrastructure.messaging.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hengyi.japp.fund.application.ApplicationEvents;
import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.share.CURDEntity;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.security.Principal;

import static com.github.ixtf.japp.core.Constant.MAPPER;
import static com.hengyi.japp.fund.infrastructure.messaging.jms.JmsApplicationEvents.QUEUE;
import static com.hengyi.japp.fund.infrastructure.messaging.jms.JmsApplicationEvents.TOPIC;

/**
 * Created by jzb on 16-10-20.
 */
@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = QUEUE,
                interfaceName = "javax.jms.Queue",
                destinationName = "jappFundServer_Queue",
                description = "jappFund Queue"),
        @JMSDestinationDefinition(
                name = TOPIC,
                interfaceName = "javax.jms.Topic",
                destinationName = "jappFundServer_Topic",
                description = "jappFund Topic"),
})
@ApplicationScoped
public class JmsApplicationEvents implements ApplicationEvents {
    public static final String QUEUE = "java:/jms/jappFundServer_Queue";
    public static final String TOPIC = "java:/jms/jappFundServer_Topic";
    @Inject
    private Logger log;
    @Inject
    private JMSContext jmsContext;
    @Resource(mappedName = QUEUE)
    private Queue queue;
    @Resource(mappedName = TOPIC)
    private Topic topic;

    @Override
    public <T extends CURDEntity> void curdEvent(Principal principal, Class<T> entiyClass, Object id, EventType eventType, Object command) {
        try {
            MapMessage message = jmsContext.createMapMessage();
            message.setString("principal", principal.getName());
            message.setString("entityClass", entiyClass.getName());
            message.setObject("id", id);
            message.setString("eventType", eventType.name());
            if (command != null) {
                message.setString("command", MAPPER.writeValueAsString(command));
            }
            jmsContext.createProducer().send(topic, message);
        } catch (JMSException | JsonProcessingException e) {
            log.error("", e);
        }
    }

    @Override
    public <T extends CURDEntity> void curdEvent(Principal principal, Class<T> entiyClass, Object id, EventType eventType) {
        curdEvent(principal, entiyClass, id, eventType, null);
    }

}
