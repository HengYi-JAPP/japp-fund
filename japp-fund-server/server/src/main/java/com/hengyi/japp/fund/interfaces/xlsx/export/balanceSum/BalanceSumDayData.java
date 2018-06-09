package com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jzb.J;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class BalanceSumDayData extends BaseFillData {
    private final BalanceSumSheetData sheetData;
    private final int colIndex;
    private final LocalDate ld;
    private final Map<Corporation, Balancelike> balanceMap;

    BalanceSumDayData(BalanceSumSheetData sheetData, LocalDate ld, Collection<Balancelike> balances) {
        super(sheetData.getSheet());
        this.sheetData = sheetData;
        this.ld = ld;
        colIndex = ld.getDayOfWeek().getValue() + 1;
        this.balanceMap = balances.stream()
                .parallel()
                .filter(fiBalance -> ld.isEqual(fiBalance.localDate()))
                .collect(toMap(Balancelike::getCorporation, Function.identity()));
    }

    public void fillData(final Row dateRow, final Map<BalanceSumType, Row> sumTypeRowMap, final Row todayBalanceRow) {
        Cell cell = cell(dateRow, colIndex);
        cell.setCellValue(ld.getDayOfMonth() + "号");

        sumTypeRowMap.entrySet().forEach(this::fillBalanceData);

        cell = cell(todayBalanceRow, colIndex);
        double d = balanceMap.values()
                .stream()
                .parallel()
                .map(Balancelike::getBalance)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        money(cell, d);
    }

    private void fillBalanceData(Map.Entry<BalanceSumType, Row> entry) {
        BalanceSumType sumType = entry.getKey();
        Row sumTypeRow = entry.getValue();

        Set<Corporation> corporations = sheetData.getCorporations(sumType);
        Iterator<Corporation> it = corporations.iterator();
        int i = 0;
        while (it.hasNext()) {
            int rowIndex = sumTypeRow.getRowNum() - corporations.size() + i++;
            Row row = row(rowIndex);
            Cell cell = cell(row, colIndex);
            Corporation corporation = it.next();
            Optional.ofNullable(balanceMap.get(corporation)).ifPresent(v -> money(cell, v));
        }

        Stream<Corporation> calcCorporationStream;
        if (J.nonEmpty(corporations)) {
            calcCorporationStream = corporations.stream();
        } else {
            calcCorporationStream = BalanceSumType.getAllSub(sumType)
                    .stream()
                    .parallel()
                    .map(sheetData::getCorporations)
                    .flatMap(Collection::stream);
        }
        double sumTypeBalance = calcCorporationStream
                .parallel()
                .map(balanceMap::get)
                .filter(Objects::nonNull)
                .map(Balancelike::getBalance)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        Cell cell = cell(sumTypeRow, colIndex);
        money(cell, sumTypeBalance);
    }

    LocalDate getLd() {
        return ld;
    }

}
