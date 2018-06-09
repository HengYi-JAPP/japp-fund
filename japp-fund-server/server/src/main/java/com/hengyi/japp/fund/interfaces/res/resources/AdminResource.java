package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.AdminService;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.command.InitCdhpCommand;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.interfaces.fi.CorporationSuggest;
import com.hengyi.japp.fund.interfaces.fi.CurrencySuggest;
import com.hengyi.japp.fund.interfaces.fi.FiService;
import com.hengyi.japp.fund.interfaces.oa.OaService;
import com.hengyi.japp.fund.interfaces.oa.OperatorSuggest;
import com.hengyi.japp.fund.interfaces.res.resources.admin.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("admin")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AdminResource {
    @Inject
    private AdminAppClientResource adminAppClientResource;
    @Inject
    private AdminPurposeResource adminPurposeResource;
    @Inject
    private AdminCorporationResource adminCorporationResource;
    @Inject
    private AdminCurrencyResource adminCurrencyResource;
    @Inject
    private AdminOperatorGroupResource adminOperatorGroupResource;
    @Inject
    private AdminOperatorResource adminOperatorResource;
    @Inject
    private FiService fiService;
    @Inject
    private AuthService authService;
    @Inject
    private OaService oaService;
    @Inject
    private AdminService adminService;

    @Path("appClients")
    public AdminAppClientResource appClients(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminAppClientResource;
    }

    @Path("purposes")
    public AdminPurposeResource purposes(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminPurposeResource;
    }

    @Path("corporations")
    public AdminCorporationResource corporations(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminCorporationResource;
    }

    @Path("currencies")
    public AdminCurrencyResource currencies(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminCurrencyResource;
    }

    @Path("operatorGroups")
    public AdminOperatorGroupResource operatorGroups(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminOperatorGroupResource;
    }

    @Path("operators")
    public AdminOperatorResource operators(@Context SecurityContext sc) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminOperatorResource;
    }

    @Path("suggestCorporations")
    @GET
    public Collection<CorporationSuggest> suggestCorporations(@Context SecurityContext sc,
                                                              @Valid @NotBlank @QueryParam("q") String q) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return fiService.suggestCorporations(q);
    }

    @Path("suggestCurrencies")
    @GET
    public Collection<CurrencySuggest> suggestCurrencies(@Context SecurityContext sc,
                                                         @Valid @NotBlank @QueryParam("q") String q) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return fiService.suggestCurrencies(q);
    }

    @Path("suggestOperators")
    @GET
    public Collection<OperatorSuggest> suggestOperators(@Context SecurityContext sc,
                                                        @Valid @NotBlank @QueryParam("q") String q) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return oaService.suggestOperators(q);
    }

    @Path("initCdhp")
    @POST
    public FundBalance initCdhp(@Context SecurityContext sc,
                                @Valid @NotNull InitCdhpCommand command) throws Exception {
        authService.checkAdmin(sc.getUserPrincipal());
        return adminService.initCdhp(command);
    }
}
