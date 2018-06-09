package com.hengyi.japp.fund.interfaces.res.resources.admin;

import com.hengyi.japp.fund.application.CurrencyService;
import com.hengyi.japp.fund.application.command.CurrencyImportCommand;
import com.hengyi.japp.fund.application.command.CurrencyUpdateCommand;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
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
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("currencies")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AdminCurrencyResource {
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private CurrencyService currencyService;

    @POST
    public Currency create(@Context SecurityContext sc,
                           @Valid @NotNull CurrencyImportCommand command) throws Exception {
        return currencyService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Currency update(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotNull CurrencyUpdateCommand command) throws Exception {
        return currencyService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public Currency get(@Context SecurityContext sc,
                        @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return currencyRepository.find(id);
    }

    @GET
    public Collection<Currency> list(@Context SecurityContext sc) throws Exception {
        return currencyRepository.findAll().parallel().collect(Collectors.toList());
    }
}
