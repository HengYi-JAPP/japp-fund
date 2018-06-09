package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.domain.event.EventType;
import com.hengyi.japp.fund.share.CURDEntity;

import java.security.Principal;

/**
 * Created by jzb on 16-7-5.
 */
public interface ApplicationEvents {

    <T extends CURDEntity> void curdEvent(Principal principal, Class<T> entiyClass, Object id, EventType eventType, Object command);

    <T extends CURDEntity> void curdEvent(Principal principal, Class<T> entiyClass, Object id, EventType eventType);

}
