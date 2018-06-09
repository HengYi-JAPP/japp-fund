package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.CorporationService;
import com.hengyi.japp.fund.application.command.CorporationImportCommand;
import com.hengyi.japp.fund.application.command.CorporationUpdateCommand;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
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

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AdminCorporationResource {
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CorporationService corporationService;

    @POST
    public Corporation create(@Context SecurityContext sc,
                              @Valid @NotNull CorporationImportCommand command) throws Exception {
        return corporationService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Corporation update(@Context SecurityContext sc,
                              @Valid @NotBlank @PathParam("id") String id,
                              @Valid @NotNull CorporationUpdateCommand command) throws Exception {
        return corporationService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public Corporation get(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return corporationRepository.find(id);
    }

    @GET
    public Collection<Corporation> list(@Context SecurityContext sc) throws Exception {
        return corporationRepository.findAll().parallel().collect(Collectors.toSet());
    }

}
