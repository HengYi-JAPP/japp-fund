package com.hengyi.japp.fund.client.interfaces.servlet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.security.auth.UserPrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.jzb.J;
import org.jzb.exception.JNonAuthenticationError;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by jzb on 17-3-13.
 */
public class PrincipalFilter implements Filter {
    private static Cache<String, AccessToken> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Principal getUserPrincipal() {
                try {
                    Assertion assertion = AssertionHolder.getAssertion();
                    String key = Optional.ofNullable(assertion)
                            .map(Assertion::getPrincipal)
                            .map(Principal::getName)
                            .orElse(null);
                    if (J.nonBlank(key)) {
                        AccessToken accessToken = cache.get(key, () -> new AccessToken(assertion));
                        return new UserPrincipal(accessToken.token());
                    }
                } catch (Exception e) {
                }
                throw new JNonAuthenticationError();
            }
        }, response);
    }

    @Override
    public void destroy() {
        cache.cleanUp();
    }

}

