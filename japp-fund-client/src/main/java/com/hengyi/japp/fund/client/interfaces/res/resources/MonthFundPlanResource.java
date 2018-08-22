package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Set;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("monthFundPlans")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class MonthFundPlanResource {

    @POST
    public String create(@Context SecurityContext sc,
                         @Valid @NotBlank String command) throws Exception {
        return Api.post(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS, command);
    }

    @Path("{id}")
    @PUT
    public String update(@Context SecurityContext sc,
                         @Valid @NotBlank @PathParam("id") String id,
                         @Valid @NotBlank String command) throws Exception {
        return Api.put(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS + "/" + id, command);
    }

    @Path("{id}")
    @GET
    public String get(@Context SecurityContext sc,
                      @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return Api.get(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS + "/" + id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        Api.delete(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS + "/" + id);
    }

    @GET
    public String list(@Context SecurityContext sc,
                       @Valid @NotNull @Size(min = 1) @QueryParam("corporationId") Set<String> corporationIds,
                       @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                       @Valid @Min(2016) @QueryParam("year") int year,
                       @Valid @Min(1) @Max(12) @QueryParam("month") int month) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("corporationId", corporationIds);
        builder.put("currencyId", currencyIds);
        builder.put("year", year);
        builder.put("month", month);
        return Api.get(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS, builder.build());
    }

    @Path("{id}/fundlikes")
    @POST
    public String createFund(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id,
                             @Valid @NotBlank String command) throws Exception {
        return Api.post(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS + "/" + id + "/fundlikes", command);
    }

    @Path("{id}/fundlikes/{fundId}")
    @PUT
    public String updateFund(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id,
                             @Valid @NotBlank @PathParam("fundId") String fundId,
                             @Valid @NotBlank String command) throws Exception {
        return Api.put(sc.getUserPrincipal(), Api.Urls.MONTHFUNDPLANS + "/" + id + "/fundlikes/" + fundId, command);
    }

}

