package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.CalculateFundBalanceService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.util.Set;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("reCalculateFundBalances")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ReCalculateFundBalancesResource {
    @Inject
    private CalculateFundBalanceService calculateFundBalanceService;

    @Path("byDate")
    @GET
    public void list(@Context SecurityContext sc,
                     @Valid @NotNull @Size(min = 1) @QueryParam("corporationId") Set<String> corporationIds,
                     @Valid @Min(2016) @QueryParam("sYear") int sYear,
                     @Valid @Min(1) @Max(12) @QueryParam("sMonth") int sMonth,
                     @Valid @Min(0) @Max(31) @QueryParam("sDay") int sDay,
                     @Valid @Min(2016) @QueryParam("eYear") int eYear,
                     @Valid @Min(1) @Max(12) @QueryParam("eMonth") int eMonth,
                     @Valid @Min(0) @Max(31) @QueryParam("eDay") int eDay) throws Exception {
        calculateFundBalanceService.reCalculateByDate(corporationIds, LocalDate.of(sYear, sMonth, sDay), LocalDate.of(eYear, eMonth, eDay));
    }

}
