package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("batchFundlikes")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BatchFundlikeResource {

    @POST
    public String update(@Context SecurityContext sc,
                         @Valid @NotBlank @QueryParam("monthFundPlanId") String monthFundPlanId,
                         @Valid @NotBlank String command) throws Exception {
        return Api.post(sc.getUserPrincipal(), Api.Urls.BATCH_FUNDLIKES, ImmutableMap.of("monthFundPlanId", monthFundPlanId), command);
    }

}
