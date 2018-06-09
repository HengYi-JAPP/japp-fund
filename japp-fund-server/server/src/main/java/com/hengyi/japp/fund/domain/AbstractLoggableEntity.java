package com.hengyi.japp.fund.domain;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by jzb on 16-10-20.
 */
@MappedSuperclass
public abstract class AbstractLoggableEntity extends AbstractEntity {
    @NotNull
    @ManyToOne
    private Operator creator;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;
    @NotNull
    @ManyToOne
    private Operator modifier;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDateTime;

    public void _operator(Operator operator) {
        modifier = operator;
        modifyDateTime = new Date();
        if (creator == null) {
            creator = modifier;
            createDateTime = modifyDateTime;
        }
    }

    public Operator getCreator() {
        return creator;
    }

    public void setCreator(Operator creator) {
        this.creator = creator;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Operator getModifier() {
        return modifier;
    }

    public void setModifier(Operator modifier) {
        this.modifier = modifier;
    }

    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }
}
