package com.hengyi.japp.fund.interfaces.res.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.BalancelikeService;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumType;

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
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("reports")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ReportResource {
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private AuthService authService;
    @Inject
    private BalancelikeService balancelikeService;

    @Path("sum/balances")
    @GET
    public Collection<Balancelike> balances(@Context SecurityContext sc,
                                            @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                                            @Valid @Min(2016) @QueryParam("year") int year,
                                            @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                                            @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                                            @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                                            @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        Set<Corporation> corporations = BalanceSumType.corporationIds()
                .map(corporationRepository::find)
                .collect(Collectors.toSet());
        Set<Currency> currencies = currencyIds.stream()
                .distinct()
                .map(currencyRepository::find)
                .collect(Collectors.toSet());
        LocalDate ldStart = LocalDate.of(year, month, 1);
        LocalDate ldEnd = ldStart.plusMonths(1).plusDays(-1);
        LocalDate ldDivide = LocalDate.of(divideYear, divideMonth, divideDay);
        Stream<? extends Balancelike> stream = balancelikeService.query(corporations, currencies, ldStart, ldEnd, ldDivide);
        stream = authService.filter(sc.getUserPrincipal(), stream);
        return stream.parallel().collect(Collectors.toList());
    }

    @Path("sum/balances/config")
    @GET
    public JsonNode sumTypes(@Context SecurityContext sc) throws Exception {
        return BalanceSumType.getNODE();
    }

}
