package com.hengyi.japp.fund.client.interfaces.servlet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.security.auth.UserPrincipal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;


/**
 * Created by jzb on 17-3-13.
 */
public class LocalPrincipalFilter implements Filter {
    private static Cache<String, AccessToken> cache = CacheBuilder.newBuilder()
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
                // 金赵波
                return new UserPrincipal("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqqViouTVKyUkpMyc3MU6oFAAAA__8.eERt_StmOPdJaRSm8ffuR25brkr8gwJo8UQjxrWd48zUtsL12gUZUflsk8tHoQ8L6lVdsdfrgpNTblALqodRsQ");
                // 陈
//                return new UserPrincipal("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqqViouTVKyUnIyTM3PKCozNk_KzAj2DcgqrQpMKQlRqgUAAAD__w.UiE9K9H8UmS0wXwEGQbnL3li6401t6mQvq1GavuE-Y_4U22DIR7bmptsmNyBIgAsRKwCrFMSFyWr2o7zRr2C2Q");
            }
        }, response);
    }

    @Override
    public void destroy() {
        cache.cleanUp();
    }

}