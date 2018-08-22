package com.hengyi.japp.fund.application.internal;

import com.github.ixtf.japp.core.J;
import com.github.ixtf.japp.core.exception.JAuthorizationException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.domain.AppClient;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.permission.Permission;
import com.hengyi.japp.fund.domain.permission.RoleType;
import com.hengyi.japp.fund.domain.repository.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class AuthServiceImpl implements AuthService {
    private static final Cache<String, Multimap<Corporation, Currency>> permissionsCache = CacheBuilder.newBuilder().maximumSize(100).build();
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private OperatorPermissionRepository operatorPermissionRepository;
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private AppClientRepository appClientRepository;

    @Override
    public Multimap<Corporation, Currency> getPermissions(Principal principal) throws Exception {
        return permissionsCache.get(principal.getName(), () -> {
            Collection<Permission> permissions = operatorPermissionRepository.getPermissions(principal);
            if (J.isEmpty(permissions)) {
                return HashMultimap.create();
            }

            Multimap<Corporation, Currency> result = HashMultimap.create();
            Map<String, Corporation> allCorporationMap = corporationRepository.findAll()
                    .parallel()
                    .collect(Collectors.toMap(Corporation::getId, Function.identity()));
            Map<String, Currency> allCurrencyMap = currencyRepository.findAll()
                    .parallel()
                    .collect(Collectors.toMap(Currency::getId, Function.identity()));

            permissions.forEach(permission -> {
                Set<Corporation> corporations = get(allCorporationMap, permission.isAllCorporation(), permission.getCorporationIds());
                corporations.forEach(corporation -> {
                    Map<String, Currency> currencyMap = allCurrencyMap;
                    if (J.nonEmpty(corporation.getCurrencies())) {
                        currencyMap = corporation.getCurrencies().stream()
                                .parallel()
                                .collect(Collectors.toMap(Currency::getId, Function.identity()));
                    }
                    Collection<Currency> currencies = get(currencyMap, permission.isAllCurrency(), permission.getCurrencyIds());

                    result.putAll(corporation, currencies);
                });
            });
            return result;
        });
    }

    private <T> Set<T> get(Map<String, T> map, boolean isAll, Collection<String> ids) {
        return isAll ? Sets.newHashSet(map.values()) : ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public <T extends AuthFilterable> Stream<T> filter(Principal principal, Stream<T> stream) {
        return stream.parallel().filter(it -> {
            Corporation corporation = it.getCorporation();
            Currency currency = it.getCurrency();
            try {
                checkPermission(principal, corporation, currency, RoleType.FUND_EAD);
                return true;
            } catch (Throwable e) {
                return false;
            }
        });
    }

    @Override
    public String jwtToken(String appId, String appSecret, String casPrincipal, String oaId) throws JAuthorizationException {
        AppClient appClient = appClientRepository.findByAppIdAndAppSecret(appId, appSecret);
        if (appClient == null) {
            throw new JAuthorizationException();
        }
        Operator operator = operatorRepository.findByCas(casPrincipal, oaId);
        if (operator == null) {
            throw new JAuthorizationException();
        }
        return Util.jwtToken(operator);
    }

    private boolean isAdmin(Principal principal) {
        if ("admin".equals(principal.getName())) {
            return true;
        }
        Operator operator = operatorRepository.findBy(principal);
        return operator.isAdmin();
    }

    @Override
    public void checkAdmin(Principal principal) throws JAuthorizationException {
        Operator operator = operatorRepository.find(principal.getName());
        if (!operator.isAdmin()) {
            throw new JAuthorizationException();
        }
    }

    @Override
    public void checkPermission(Principal principal, Corporation corporation, Currency currency, RoleType roleType) throws Exception {
        if (isAdmin(principal)) {
            return;
        }
        Multimap<Corporation, Currency> multimap = getPermissions(principal);
        Collection<Currency> currencies = multimap.get(corporation);
        if (J.nonEmpty(currencies) && currencies.contains(currency)) {
            return;
        }
        throw new JAuthorizationException();
    }

    @Override
    public void permissionsCacheClear() {
        permissionsCache.invalidateAll();
    }

}
