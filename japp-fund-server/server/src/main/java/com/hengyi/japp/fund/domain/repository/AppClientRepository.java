package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.AppClient;
import com.hengyi.japp.fund.share.CURDEntityRepository;

/**
 * Created by jzb on 16-10-28.
 */
public interface AppClientRepository extends CURDEntityRepository<AppClient, String> {
    AppClient findByAppId(String appId);

    AppClient findByAppIdAndAppSecret(String appId, String appSecret);
}
