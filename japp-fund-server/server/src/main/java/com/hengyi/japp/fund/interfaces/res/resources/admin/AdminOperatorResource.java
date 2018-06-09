package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.OperatorService;
import com.hengyi.japp.fund.application.command.OperatorImportCommand;
import com.hengyi.japp.fund.application.command.OperatorUpdateCommand;
import com.hengyi.japp.fund.domain.Operator;
import com.hengyi.japp.fund.domain.permission.OperatorPermission;
import com.hengyi.japp.fund.domain.repository.OperatorPermissionRepository;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
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
 * Created by jzb on 16-10-28.
 */
@Stateless
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AdminOperatorResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private OperatorPermissionRepository operatorPermissionRepository;
    @Inject
    private OperatorService operatorService;

    @POST
    public Operator create(@Context SecurityContext sc,
                           @Valid @NotNull OperatorImportCommand command) throws Exception {
        return operatorService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @GET
    public Operator get(@Context SecurityContext sc,
                        @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return operatorRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        operatorRepository.delete(id);
    }

    @GET
    public Collection<Operator> list(@Context SecurityContext sc) throws Exception {
        return operatorRepository.findAll().parallel().collect(Collectors.toList());
    }

    @Path("{id}")
    @PUT
    public OperatorPermission update(@Context SecurityContext sc,
                                     @Valid @NotBlank @PathParam("id") String id,
                                     @Valid @NotNull OperatorUpdateCommand command) throws Exception {
        return operatorService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/permission")
    @GET
    public OperatorPermission permission(@Context SecurityContext sc,
                                         @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return operatorPermissionRepository.find(id);
    }
}
