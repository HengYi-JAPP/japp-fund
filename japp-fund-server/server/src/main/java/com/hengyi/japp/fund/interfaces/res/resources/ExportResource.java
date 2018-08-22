package com.hengyi.japp.fund.interfaces.res.resources;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.ExportService;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import java.net.URLEncoder;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private ExportService exportService;
    @Inject
    private AuthService authService;

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
        final Principal principal = sc.getUserPrincipal();
        Multimap<Corporation, Currency> authMap = authService.getPermissions(principal);
        Set<Corporation> corporations = J.isEmpty(corporationIds) ? authMap.keySet() : corporationIds.stream()
                .distinct()
                .map(corporationRepository::find)
                .filter(authMap::containsKey)
                .collect(Collectors.toSet());
        Set<Currency> currencies = J.isEmpty(currencyIds) ? Sets.newHashSet(authMap.values()) : currencyIds.stream()
                .distinct()
                .map(currencyRepository::find)
                .collect(Collectors.toSet());
        LocalDate ldDivide = LocalDate.of(divideYear, divideMonth, divideDay);
        StreamingOutput result = exportService.export(principal, corporations, currencies, year, month, ldDivide);
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
        final Principal principal = sc.getUserPrincipal();
        Multimap<Corporation, Currency> authMap = authService.getPermissions(principal);
        Set<Corporation> corporations = J.isEmpty(corporationIds) ? authMap.keySet() : corporationIds.stream()
                .distinct()
                .map(corporationRepository::find)
                .filter(authMap::containsKey)
                .collect(Collectors.toSet());
        Set<Currency> currencies = J.isEmpty(currencyIds) ? Sets.newHashSet(authMap.values()) : currencyIds.stream()
                .distinct()
                .map(currencyRepository::find)
                .collect(Collectors.toSet());
        LocalDate ldDivide = LocalDate.of(divideYear, divideMonth, divideDay);
        StreamingOutput result = exportService.exportSum(principal, corporations, currencies, year, month, ldDivide);
        String encodeDownloadName = URLEncoder.encode("合并报表-" + year + "-" + month + "【" + divideYear + "-" + divideMonth + "-" + divideDay + "】.xlsx", UTF_8.name());
        return Response.ok(result)
                .header("Content-Disposition", "attachment;filename=" + encodeDownloadName)
                .build();
    }

}
