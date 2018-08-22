package com.hengyi.japp.fund.interfaces.res.resources;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.application.MonthFundPlanService;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.application.command.MonthFundPlanUpdateCommand;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.domain.MonthFundPlan;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.domain.repository.MonthFundPlanRepository;
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
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("monthFundPlans")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class MonthFundPlanResource {
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private AuthService authService;
    @Inject
    private MonthFundPlanRepository monthFundPlanRepository;
    @Inject
    private MonthFundPlanService monthFundPlanService;
    @Inject
    private FundlikeService fundlikeService;

    @POST
    public MonthFundPlan create(@Context SecurityContext sc,
                                @Valid @NotNull MonthFundPlanUpdateCommand command) throws Exception {
        return monthFundPlanService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public MonthFundPlan update(@Context SecurityContext sc,
                                @Valid @NotBlank @PathParam("id") String id,
                                @Valid @NotNull MonthFundPlanUpdateCommand command) throws Exception {
        return monthFundPlanService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public MonthFundPlan get(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return monthFundPlanRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc,
                       @Valid @NotBlank @PathParam("id") String id) throws Exception {
        monthFundPlanService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<MonthFundPlan> list(@Context SecurityContext sc,
                                          @Valid @NotNull @Size(min = 1) @QueryParam("corporationId") Set<String> corporationIds,
                                          @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                                          @Valid @Min(2016) @QueryParam("year") int year,
                                          @Valid @Min(1) @Max(12) @QueryParam("month") int month) throws Exception {
        Set<Corporation> corporations = corporationIds.stream()
                .distinct()
                .map(corporationRepository::find)
                .collect(Collectors.toSet());
        Set<Currency> currencies = currencyIds.stream()
                .distinct()
                .map(currencyRepository::find)
                .collect(Collectors.toSet());
        Stream<MonthFundPlan> stream = monthFundPlanRepository.query(corporations, currencies, year, month);
        stream = authService.filter(sc.getUserPrincipal(), stream);
        Collection<MonthFundPlan> result = stream.parallel().collect(Collectors.toList());
        if (J.nonEmpty(result)) {
            return result;
        }
        return monthFundPlanService.listAnaywary(sc.getUserPrincipal(), corporationIds, currencyIds, year, month)
                .collect(Collectors.toList());
    }

    @Path("{id}/fundlikes")
    @POST
    public Fundlike createFund(@Context SecurityContext sc,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotNull FundlikeUpdateCommand command) throws Exception {
        return fundlikeService.create(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/fundlikes/{fundId}")
    @PUT
    public Fundlike updateFund(@Context SecurityContext sc,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotBlank @PathParam("fundId") String fundId,
                               @Valid @NotNull FundlikeUpdateCommand command) throws Exception {
        return fundlikeService.update(sc.getUserPrincipal(), id, fundId, command);
    }

}
