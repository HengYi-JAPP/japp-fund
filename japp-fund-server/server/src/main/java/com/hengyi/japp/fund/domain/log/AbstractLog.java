package com.hengyi.japp.fund.domain.log;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by jzb on 17-5-10.
 */
@MappedSuperclass
public abstract class AbstractLog implements Serializable {
    @Id
    @Column(length = 36)
    protected String id;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date startDateTime = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    protected Date endDateTime;
    @Lob
    protected String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLog that = (AbstractLog) o;
        if (id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
