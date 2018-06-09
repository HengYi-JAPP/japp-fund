package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("auth")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AuthResource {
    @Inject
    private AuthService authService;
    @Inject
    private OperatorRepository operatorRepository;

    @GET
    public Operator get(@Context SecurityContext sc) {
        return operatorRepository.findBy(sc.getUserPrincipal());
    }

    @Path("jwtToken")
    @GET
    @Produces(TEXT_PLAIN)
    public String get(@Valid @NotBlank @QueryParam("appId") String appId,
                      @Valid @NotBlank @QueryParam("appSecret") String appSecret,
                      @Valid @NotBlank @QueryParam("cas") String casPrincipal,
                      @QueryParam("oaId") String oaId) throws Exception {
        return authService.jwtToken(appId, appSecret, casPrincipal, oaId);
    }

}
