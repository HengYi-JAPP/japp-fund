package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hengyi.japp.fund.client.Api;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.ixtf.japp.core.Constant.MAPPER;
import static com.hengyi.japp.fund.client.Api.Urls.REPORTS_SUM_BALANCES_CONFIG;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("reports")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ReportResource {
    /**
     * 合并报表专用，无需传公司
     */
    @Path("sum/balances")
    @GET
    public String sumBalances(@Context SecurityContext sc,
                              @Valid @NotNull @Size(min = 1) @QueryParam("currencyId") Set<String> currencyIds,
                              @Valid @Min(2016) @QueryParam("year") int year,
                              @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                              @Valid @Min(0) @Max(31) @QueryParam("day") int day,
                              @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                              @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                              @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("currencyId", currencyIds);
        builder.put("year", year);
        builder.put("month", month);
        builder.put("day", day);
        builder.put("divideYear", divideYear);
        builder.put("divideMonth", divideMonth);
        builder.put("divideDay", divideDay);
        return Api.get(sc.getUserPrincipal(), Api.Urls.REPORTS_SUM_BALANCES, builder.build());
    }

    /**
     * 合并报表专用，合计，小计类型
     */
    @Path("sum/sumTypes")
    @GET
    public String sumTypes(@Context SecurityContext sc) throws Exception {
        final LinkedHashMap<String, JsonNode> map = Maps.newLinkedHashMap();
        final String res = Api.get(sc.getUserPrincipal(), REPORTS_SUM_BALANCES_CONFIG);
        MAPPER.readTree(res).fields().forEachRemaining(it -> {
            final ObjectNode node = MAPPER.createObjectNode();
            final String key = it.getKey();
            map.put(key,node);
            node.put("type", key);
            final JsonNode sumType = it.getValue();
            node.set("name", sumType.get("displayName"));
            node.set("corporationIds", sumType.get("corporationIds"));
        });
        final List<JsonNode> result = Stream.of("SS_XS", "SS_NB", "SS", "FSS_JTKG", "FSS_JTCG", "FSS")
                .map(map::get)
                .collect(Collectors.toList());
        return MAPPER.writeValueAsString(result);
    }

}
