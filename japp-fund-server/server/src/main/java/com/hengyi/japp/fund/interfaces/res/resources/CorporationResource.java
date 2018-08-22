package com.hengyi.japp.fund.interfaces.res.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ixtf.japp.core.J;
import com.google.common.collect.Multimap;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.CorporationService;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.github.ixtf.japp.core.Constant.MAPPER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("corporations")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class CorporationResource {
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CorporationService corporationService;
    @Inject
    private AuthService authService;

    @Path("{id}")
    @GET
    public Corporation get(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return corporationRepository.find(id);
    }

    @GET
    public Collection<JsonNode> list(@Context SecurityContext sc) throws Exception {
        Multimap<Corporation, Currency> authMap = authService.getPermissions(sc.getUserPrincipal());
        return authMap.keySet()
                .stream()
                .parallel()
                .map(corporation -> {
                    ObjectNode node = MAPPER.convertValue(corporation, ObjectNode.class);
                    ArrayNode currencies = MAPPER.createArrayNode();
                    J.emptyIfNull(authMap.get(corporation))
                            .stream()
                            .map(currency -> MAPPER.convertValue(currency, ObjectNode.class))
                            .forEach(currencies::add);
                    node.set("currencies", currencies);
                    return node;
                })
                .collect(Collectors.toList());
    }

    @Path("{id}/currencies")
    @GET
    public Collection<Currency> listCurrency(@Context SecurityContext sc,
                                             @Valid @NotBlank @PathParam("id") String id) throws Exception {
        Corporation corporation = corporationRepository.find(id);
        Multimap<Corporation, Currency> authMap = authService.getPermissions(sc.getUserPrincipal());
        return authMap.get(corporation);
    }

}
