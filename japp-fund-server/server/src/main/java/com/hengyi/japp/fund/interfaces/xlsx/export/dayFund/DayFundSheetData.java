package com.hengyi.japp.fund.interfaces.xlsx.export.dayFund;

import com.google.common.collect.ComparisonChain;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.jzb.J;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.*;


public class DayFundSheetData extends BaseFillData implements Comparable<DayFundSheetData> {
    private final Corporation corporation;
    private final Currency currency;
    private final int year;
    private final int month;

    private Collection<Balancelike> balances;
    private Collection<Fundlike> funds;

    public DayFundSheetData(Workbook wb, Corporation corporation, Currency currency, int year, int month) {
        super(wb);
        this.corporation = corporation;
        this.currency = currency;
        this.year = year;
        this.month = month;
    }

    public void fillData(Collection<Balancelike> allBalances, Collection<Fundlike> allFunds) {
        balances = allBalances.stream()
                .parallel()
                .filter(it -> corporation.equals(it.getCorporation()) && currency.equals(it.getCurrency()))
                .collect(Collectors.toList());
        if (J.isEmpty(balances)) {
            return;
        }
        funds = allFunds.stream()
                .parallel()
                .filter(it -> corporation.equals(it.getCorporation()) && currency.equals(it.getCurrency()))
                .collect(Collectors.toList());
        if (J.isEmpty(funds)) {
            final boolean present = balances.stream()
                    .parallel()
                    .map(Balancelike::getBalance)
                    .map(BigDecimal::doubleValue)
                    .filter(it -> it > 0)
                    .findFirst()
                    .isPresent();
            if (!present) {
                return;
            }
        }

        String sheetName = corporation.getName() + currency.getCode();
        sheet = wb.createSheet(sheetName);

        int rowIndex = fillHeadData();

        int weekIndex = 1;
        for (Collection<LocalDate> lds : generateWeeks(year, month)) {
            DayFundWeekData weekData = new DayFundWeekData(sheet, weekIndex++, lds, balances, funds);
            rowIndex = weekData.fillData(rowIndex);
        }

        IntStream.rangeClosed(0, 7)
                .map(it -> it * 2 + 1)
                .forEach(sheet::autoSizeColumn);
    }

    private int fillHeadData() {
        sheet.createFreezePane(2, 3);

        Cell cell = cell(0, 0);
        cell.setCellValue(year + "年" + month + "月" + corporation.getName() + "[" + currency.getName() + "]");
        cssHead1C.accept(cell);
        head1Region(0, 0, 0, 15);
        head1Region(1, 2, 0, 1);

        for (int i = 0; i < WEEK_NAMES.length; i++) {
            final int cId = i * 2 + 2;
            cell = cell(1, cId);
            cell.setCellValue(WEEK_NAMES[i]);
            cssHead2C.accept(cell);
            weekRegion(1, 1, cId, cId + 1);

            cell = cell(2, cId);
            cell.setCellValue("金额");
            cssHead3C.accept(cell);
            cell = cell(2, cId + 1);
            cell.setCellValue("计划简述");
            cssHead3C.accept(cell);
        }
        return 3;
    }

    @Override
    public int compareTo(@NotNull DayFundSheetData o) {
        return ComparisonChain.start()
                .compare(corporation, o.corporation)
                .compare(currency, o.currency)
                .result();
    }
}
