package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.AdminService;
import com.hengyi.japp.fund.application.PurposeService;
import com.hengyi.japp.fund.application.command.PurposeUpdateCommand;
import com.hengyi.japp.fund.domain.Purpose;
import com.hengyi.japp.fund.domain.repository.PurposeRepository;
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
public class AdminPurposeResource {
    @Inject
    private PurposeRepository purposeRepository;
    @Inject
    private AdminService adminService;
    @Inject
    private PurposeService purposeService;

    @POST
    public Purpose create(@Context SecurityContext sc,
                          @Valid @NotNull PurposeUpdateCommand command) throws Exception {
        return purposeService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Purpose update(@Context SecurityContext sc,
                          @Valid @NotBlank @PathParam("id") String id,
                          @Valid @NotNull PurposeUpdateCommand command) throws Exception {
        return purposeService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public Purpose get(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return purposeRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        adminService.deleteAppClient(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<Purpose> list(@Context SecurityContext sc) throws Exception {
        return purposeRepository.findAll().collect(Collectors.toList());
    }
}
