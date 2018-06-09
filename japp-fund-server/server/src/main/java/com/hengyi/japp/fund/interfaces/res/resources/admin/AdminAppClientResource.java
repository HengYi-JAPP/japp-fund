package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.AdminService;
import com.hengyi.japp.fund.application.command.AppClientUpdateCommand;
import com.hengyi.japp.fund.domain.AppClient;
import com.hengyi.japp.fund.domain.repository.AppClientRepository;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AdminAppClientResource {
    @Inject
    private AppClientRepository appClientRepository;
    @Inject
    private AdminService adminService;

    @POST
    public AppClient create(@Context SecurityContext sc,
                            @Valid @NotNull AppClientUpdateCommand command) throws Exception {
        return adminService.createAppClient(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public AppClient update(@Context SecurityContext sc,
                            @Valid @NotBlank @PathParam("id") String id,
                            @Valid @NotNull AppClientUpdateCommand command) throws Exception {
        return adminService.updateAppClient(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public AppClient get(@Context SecurityContext sc,
                         @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return appClientRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        adminService.deleteAppClient(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<AppClient> list(@Context SecurityContext sc) throws Exception {
        return appClientRepository.findAll().parallel().collect(Collectors.toList());
    }
}
