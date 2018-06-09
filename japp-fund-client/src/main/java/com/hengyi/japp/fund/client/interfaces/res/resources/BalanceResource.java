package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;

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

@Path("balances")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BalanceResource {

    @GET
    public String balances(@Context SecurityContext sc,
                           @Valid @NotNull @Size(min = 1) @QueryParam("corporationId") Set<String> corporationIds,
                           @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                           @Valid @Min(2016) @QueryParam("year") int year,
                           @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                           @Valid @Min(0) @Max(31) @QueryParam("day") int day,
                           @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                           @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                           @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("corporationId", corporationIds);
        builder.put("currencyId", currencyIds);
        builder.put("year", year);
        builder.put("month", month);
        builder.put("day", day);
        builder.put("divideYear", divideYear);
        builder.put("divideMonth", divideMonth);
        builder.put("divideDay", divideDay);
        return Api.get(sc.getUserPrincipal(), Api.Urls.BALANCES, builder.build());
    }

}
