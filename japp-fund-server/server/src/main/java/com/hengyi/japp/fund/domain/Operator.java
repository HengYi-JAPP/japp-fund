package com.hengyi.japp.fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_OPERATOR")
@NamedQueries({
        @NamedQuery(name = "Operator.findByOaId", query = "SELECT o FROM Operator o WHERE o.oaId=:oaId AND o.deleted=FALSE"),
        @NamedQuery(name = "Operator.findByHrId", query = "SELECT o FROM Operator o WHERE o.hrId=:hrId AND o.deleted=FALSE"),
})
public class Operator extends AbstractEntity implements CURDEntity<String> {
    @NotBlank
    @Column(length = 50)
    private String name;
    @JsonIgnore
    @XmlTransient
    private String oaId;
    @JsonIgnore
    @XmlTransient
    private String hrId;
    @JsonIgnore
    @XmlTransient
    private String qyWxId;
    private String avatar;
    private boolean admin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOaId() {
        return oaId;
    }

    public void setOaId(String oaId) {
        this.oaId = oaId;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getQyWxId() {
        return qyWxId;
    }

    public void setQyWxId(String qyWxId) {
        this.qyWxId = qyWxId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return name;
    }
}
