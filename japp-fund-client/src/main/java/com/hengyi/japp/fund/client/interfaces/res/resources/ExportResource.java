package com.hengyi.japp.fund.client.interfaces.res.resources;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;
import okhttp3.Request;

import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import java.net.URLEncoder;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("exports")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ExportResource {

    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response get(@Context SecurityContext sc,
                        @QueryParam("corporationId") Set<String> corporationIds,
                        @QueryParam("currencyId") Set<String> currencyIds,
                        @Valid @Min(2016) @QueryParam("year") int year,
                        @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                        @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                        @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                        @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("corporationId", corporationIds);
        builder.put("currencyId", currencyIds);
        builder.put("year", year);
        builder.put("month", month);
        builder.put("divideYear", divideYear);
        builder.put("divideMonth", divideMonth);
        builder.put("divideDay", divideDay);
        Request request = Api.buildGet(sc.getUserPrincipal(), Api.Urls.EXPORTS, builder.build());
        StreamingOutput result = Api.download(request);
        String encodeDownloadName = URLEncoder.encode("每日动态-" + year + "-" + month + "【" + divideYear + "-" + divideMonth + "-" + divideDay + "】.xlsx", UTF_8.name());
        return Response.ok(result)
                .header("Content-Disposition", "attachment;filename=" + encodeDownloadName)
                .build();
    }

    @Path("sumReports")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response sumReport(@Context SecurityContext sc,
                              @QueryParam("corporationId") Set<String> corporationIds,
                              @QueryParam("currencyId") Set<String> currencyIds,
                              @Valid @Min(2016) @QueryParam("year") int year,
                              @Valid @Min(1) @Max(12) @QueryParam("month") int month,
                              @Valid @Min(2016) @QueryParam("divideYear") int divideYear,
                              @Valid @Min(1) @Max(12) @QueryParam("divideMonth") int divideMonth,
                              @Valid @Min(1) @Max(31) @QueryParam("divideDay") int divideDay) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("corporationId", corporationIds);
        builder.put("currencyId", currencyIds);
        builder.put("year", year);
        builder.put("month", month);
        builder.put("divideYear", divideYear);
        builder.put("divideMonth", divideMonth);
        builder.put("divideDay", divideDay);
        Request request = Api.buildGet(sc.getUserPrincipal(), Api.Urls.EXPORTS + "/sumReports", builder.build());
        StreamingOutput result = Api.download(request);
        String encodeDownloadName = URLEncoder.encode("合并报表-" + year + "-" + month + "【" + divideYear + "-" + divideMonth + "-" + divideDay + "】.xlsx", UTF_8.name());
        return Response.ok(result)
                .header("Content-Disposition", "attachment;filename=" + encodeDownloadName)
                .build();
    }

}
