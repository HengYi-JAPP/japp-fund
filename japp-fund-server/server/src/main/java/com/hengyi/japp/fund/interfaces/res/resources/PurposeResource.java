package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.PurposeService;
import com.hengyi.japp.fund.domain.Purpose;
import com.hengyi.japp.fund.domain.repository.PurposeRepository;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
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
@Path("purposes")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PurposeResource {
    @Inject
    private PurposeRepository purposeRepository;
    @Inject
    private PurposeService purposeService;

    @GET
    public Collection<Purpose> list(@Context SecurityContext sc) {
        return purposeRepository.findAll().parallel().collect(Collectors.toList());
    }

    @Path("{id}")
    @GET
    public Purpose get(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return purposeRepository.find(id);
    }

}
