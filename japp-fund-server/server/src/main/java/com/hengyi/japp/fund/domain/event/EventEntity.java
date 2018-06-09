package com.hengyi.japp.fund.domain.event;

import com.hengyi.japp.fund.domain.AbstractEntity;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.ee.JEE;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by jzb on 16-10-20.
 */
@MappedSuperclass
public abstract class EventEntity extends AbstractEntity {
    @NotBlank
    @Column(length = 36)
    protected String entityId;
    @NotNull
    @ManyToOne
    protected Operator operator;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateTime;
    @Lob
    protected String command;
    @NotNull
    @Enumerated(EnumType.STRING)
    protected EventType eventType;

    public void from(MapMessage msg) throws JMSException {
        entityId = msg.getString("id");
        OperatorRepository operatorRepository = JEE.getBean(OperatorRepository.class);
        operator = operatorRepository.find(msg.getString("principal"));
        dateTime = new Date();
        command = msg.getString("command");
        eventType = EventType.valueOf(msg.getString("eventType"));
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
