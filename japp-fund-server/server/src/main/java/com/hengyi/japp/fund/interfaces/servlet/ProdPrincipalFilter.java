package com.hengyi.japp.fund.interfaces.servlet;

import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import org.jzb.J;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

/**
 * Created by jzb on 17-4-15.
 */

public class ProdPrincipalFilter implements Filter {
    @Inject
    private OperatorRepository operatorRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Principal getUserPrincipal() {
                String auth = getHeader(HttpHeaders.AUTHORIZATION);
                String jwtToken = J.substring(auth, 7);
                // 如果在 head 中没有 token，则检查 queryParam
                if (J.isBlank(jwtToken)) {
                    jwtToken = getParameter("token");
                }

                return Optional.ofNullable(jwtToken)
                        .map(Util::claims)
                        .map(Claims::getSubject)
                        .map(UserPrincipal::new)
                        .orElseThrow(null);
            }
        }, response);
    }

    @Override
    public void destroy() {

    }
}
