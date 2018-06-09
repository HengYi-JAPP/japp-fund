package com.hengyi.japp.fund.infrastructure.messaging.jms;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
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
public class OperatorPermissionMdb implements MessageListener {
    @Inject
    private Logger log;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private AuthService authService;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(OperatorPermission.class.getName(), entityClass)) {
                return;
            }
            authService.permissionsCacheClear();
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }
}
