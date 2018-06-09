package com.hengyi.japp.fund.application;

import com.google.common.collect.Multimap;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.permission.RoleType;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-11-19.
 */
public interface AuthService {
    Multimap<Corporation, Currency> getPermissions(Principal principal) throws Exception;

    <T extends AuthFilterable> Stream<T> filter(Principal principal, Stream<T> stream) throws Exception;

    void checkPermission(Principal principal, Corporation corporation, Currency currency, RoleType roleType) throws Exception;

    void checkAdmin(Principal principal) throws Exception;

    String jwtToken(String appId, String appSecret, String casPrincipal, String oaId) throws Exception;

    void permissionsCacheClear();

    interface AuthFilterable {

        Corporation getCorporation();

        Currency getCurrency();
    }
}