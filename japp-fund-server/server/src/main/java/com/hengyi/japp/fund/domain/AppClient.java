package com.hengyi.japp.fund.domain;

import com.hengyi.japp.fund.share.CURDEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_APPCLIENT")
@NamedQueries({
        @NamedQuery(name = "AppClient.findByAppId", query = "SELECT o FROM AppClient o WHERE o.appId=:appId AND o.deleted=FALSE"),
        @NamedQuery(name = "AppClient.findByAppIdAndAppSecret", query = "SELECT o FROM AppClient o WHERE o.appId=:appId AND o.appSecret=:appSecret AND o.deleted=FALSE"),
})
public class AppClient extends AbstractLoggableEntity implements CURDEntity<String> {
    @NotBlank
    @Column(unique = true, length = 50)
    private String appId;
    @NotBlank
    private String appSecret;
    @NotBlank
    @Column(length = 50)
    private String name;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
