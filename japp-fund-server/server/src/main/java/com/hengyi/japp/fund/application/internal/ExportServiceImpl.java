package com.hengyi.japp.fund.application.internal;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hengyi.japp.fund.application.AuthService;
import com.hengyi.japp.fund.application.BalancelikeService;
import com.hengyi.japp.fund.application.ExportService;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumSheetData;
import com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumType;
import com.hengyi.japp.fund.interfaces.xlsx.export.dayFund.DayFundSheetData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.StreamingOutput;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-11-19.
 */
@Stateless
public class ExportServiceImpl implements ExportService {
    @Inject
    private BalancelikeService balanceService;
    @Inject
    private FundlikeService fundlikeService;
    @Inject
    private AuthService authService;

    @Override
    public StreamingOutput export(final Principal principal, final Set<Corporation> corporations, final Set<Currency> currencies, final int year, final int month, final LocalDate ldDivide) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Multimap<Corporation, Currency> authMap = authService.getPermissions(principal);
        final LocalDate ldStart = LocalDate.of(year, month, 1);
        final LocalDate ldEnd = ldStart.plusMonths(1).plusDays(-1);
        final Stream<? extends Balancelike> balancesStream = balanceService.query(corporations, currencies, ldStart.plusDays(-1), ldEnd, ldDivide);
        Collection<Balancelike> balances = authService.filter(principal, balancesStream).parallel().collect(Collectors.toSet());
        final Stream<? extends Fundlike> fundsStream = fundlikeService.query(corporations, currencies, ldStart, ldEnd, ldDivide);
        Collection<Fundlike> funds = authService.filter(principal, fundsStream).parallel().collect(Collectors.toSet());
        corporations.stream()
                .filter(authMap::containsKey)
                .distinct()
                .sorted()
                .flatMap(corporation -> currencies.stream()
                        .filter(it -> authMap.containsEntry(corporation, it))
                        .distinct()
                        .sorted()
                        .map(currency -> new DayFundSheetData(wb, corporation, currency, year, month))
                )
                .forEach(sheetData -> sheetData.fillData(balances, funds));
        return (out) -> wb.write(out);
    }

    @Override
    public StreamingOutput exportSum(final Principal principal, final Set<Corporation> corporations, final Set<Currency> currencies, final int year, final int month, final LocalDate ldDivide) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Multimap<Corporation, Currency> authMap = authService.getPermissions(principal);
        final LocalDate ldStart = LocalDate.of(year, month, 1);
        final LocalDate ldEnd = ldStart.plusMonths(1).plusDays(-1);
        Collection<String> sumCorporationIds = BalanceSumType.corporationIds().parallel().collect(Collectors.toSet());
        Map<String, Corporation> corporationMap = corporations.parallelStream()
                .distinct()
                .filter(it -> sumCorporationIds.contains(it.getId()))
                .filter(authMap::containsKey)
                .collect(Collectors.toMap(Corporation::getId, Function.identity()));
        final Stream<? extends Balancelike> balancesStream = balanceService.query(Sets.newHashSet(corporationMap.values()), currencies, ldStart, ldEnd, ldDivide);
        Collection<Balancelike> balances = balancesStream.parallel().collect(Collectors.toSet());
        currencies.stream()
                .distinct()
                .sorted()
                .map(currency -> new BalanceSumSheetData(wb, corporationMap, currency, year, month))
                .forEach(sheetData -> sheetData.fillData(balances));
        return (out) -> wb.write(out);
    }

}
