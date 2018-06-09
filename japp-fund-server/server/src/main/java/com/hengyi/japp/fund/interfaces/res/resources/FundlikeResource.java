package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.AbstractFundlikeEntity;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import org.hibernate.validator.constraints.NotBlank;

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
@Path("fundlikes")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class FundlikeResource {
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private AuthService authService;
    @Inject
    private FundlikeService fundlikeService;

    @Path("{id}")
    @PUT
    public Fundlike update(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotNull FundlikeUpdateCommand command) throws Exception {
        AbstractFundlikeEntity entity = fundlikeService.find(id);
        return fundlikeService.update(sc.getUserPrincipal(), entity.getMonthFundPlan().getId(), id, command);
    }

    @Path("{id}")
    @GET
    public Fundlike get(@Context SecurityContext sc,
                        @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return fundlikeService.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        fundlikeService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<Fundlike> funds(@Context SecurityContext sc,
                                      @Valid @NotNull @Size(min = 1) @QueryParam("corporationId") Set<String> corporationIds,
                                      @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                                      @Valid @Min(2016) @QueryParam("year") int year,
                                      @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                                      @Valid @Min(0) @Max(31) @QueryParam("day") int day,
                                      @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                                      @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                                      @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        Set<Corporation> corporations = corporationIds.stream()
                .distinct()
                .map(corporationRepository::find)
                .collect(Collectors.toSet());
        Set<Currency> currencies = currencyIds.stream()
                .distinct()
                .map(currencyRepository::find)
                .collect(Collectors.toSet());
        LocalDate ldStart;
        LocalDate ldEnd;
        if (day == 0) {
            ldStart = LocalDate.of(year, month, 1);
            ldEnd = ldStart.plusMonths(1).plusDays(-1);
        } else {
            ldStart = LocalDate.of(year, month, day);
            ldEnd = ldStart;
        }
        LocalDate ldDivide = LocalDate.of(divideYear, divideMonth, divideDay);
        Stream<? extends Fundlike> stream = fundlikeService.query(corporations, currencies, ldStart, ldEnd, ldDivide);
        stream = authService.filter(sc.getUserPrincipal(), stream);
        return stream.parallel().collect(Collectors.toList());
    }

}
