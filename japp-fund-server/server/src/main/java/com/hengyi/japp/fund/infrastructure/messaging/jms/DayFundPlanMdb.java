package com.hengyi.japp.fund.infrastructure.messaging.jms;

import com.hengyi.japp.fund.domain.DayFundPlan;
import com.hengyi.japp.fund.domain.event.DayFundPlanEvent;
import org.slf4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Objects;

/**
 * Created by jzb on 16-10-28.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JmsApplicationEvents.TOPIC),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class DayFundPlanMdb implements MessageListener {
    @Inject
    private Logger log;
    @Inject
    private EventService eventService;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, DayFundPlan.class.getName()))
                return;

            eventService.save(DayFundPlanEvent.class, msg);
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }
}
