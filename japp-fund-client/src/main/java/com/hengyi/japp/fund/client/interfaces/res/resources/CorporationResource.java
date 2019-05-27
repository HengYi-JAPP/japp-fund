package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.hengyi.japp.fund.client.Api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("corporations")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class CorporationResource {

    @Path("{id}")
    @GET
    public String get(@Context SecurityContext sc,
                      @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return Api.get(sc.getUserPrincipal(), Api.Urls.CORPORATIONS + "/" + id);
    }

    @GET
    public String list(@Context SecurityContext sc) throws Exception {
        return Api.get(sc.getUserPrincipal(), Api.Urls.CORPORATIONS);
    }

}
