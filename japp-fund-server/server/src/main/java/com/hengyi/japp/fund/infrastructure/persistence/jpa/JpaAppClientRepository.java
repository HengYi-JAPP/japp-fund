package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.AppClient;
import com.hengyi.japp.fund.domain.repository.AppClientRepository;

import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-28.
 */
public class JpaAppClientRepository extends JpaCURDRepository<AppClient, String> implements AppClientRepository, Serializable {
    @Override
    public AppClient findByAppId(String appId) {
        TypedQuery<AppClient> typedQuery = em.createNamedQuery("AppClient.findByAppId", AppClient.class)
                .setParameter("appId", appId)
                .setMaxResults(1);
        return Util.getSingle(typedQuery);
    }

    @Override
    public AppClient findByAppIdAndAppSecret(String appId, String appSecret) {
        TypedQuery<AppClient> typedQuery = em.createNamedQuery("AppClient.findByAppIdAndAppSecret", AppClient.class)
                .setParameter("appId", appId)
                .setParameter("appSecret", appSecret)
                .setMaxResults(1);
        return Util.getSingle(typedQuery);
    }
}
