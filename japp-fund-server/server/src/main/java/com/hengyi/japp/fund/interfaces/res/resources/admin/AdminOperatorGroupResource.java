package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.OperatorGroupService;
import com.hengyi.japp.fund.application.command.OperatorGroupUpdateCommand;
import com.hengyi.japp.fund.domain.permission.OperatorGroup;
import com.hengyi.japp.fund.domain.repository.OperatorGroupRepository;
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
public class AdminOperatorGroupResource {
    @Inject
    private OperatorGroupRepository operatorGroupRepository;
    @Inject
    private OperatorGroupService operatorGroupService;

    @POST
    public OperatorGroup create(@Context SecurityContext sc,
                                @Valid @NotNull OperatorGroupUpdateCommand command) throws Exception {
        return operatorGroupService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public OperatorGroup update(@Context SecurityContext sc,
                                @Valid @NotBlank @PathParam("id") String id,
                                @Valid @NotNull OperatorGroupUpdateCommand command) throws Exception {
        return operatorGroupService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public OperatorGroup get(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return operatorGroupRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        operatorGroupRepository.delete(id);
    }

    @GET
    public Collection<OperatorGroup> list(@Context SecurityContext sc) throws Exception {
        return operatorGroupRepository.findAll().parallel().collect(Collectors.toList());
    }
}
