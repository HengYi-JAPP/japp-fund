package com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.ComparisonChain;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.*;


public class BalanceSumSheetData extends BaseFillData implements Comparable<BalanceSumSheetData> {
    private final Currency currency;
    private final int year;
    private final int month;
    private final Collection<Collection<LocalDate>> weeks;
    //所有公司，包含权限和报表本身的公司范围
    private final Map<String, Corporation> corporationMap;
    private Collection<Balancelike> balances;

    public BalanceSumSheetData(Workbook wb, Map<String, Corporation> corporationMap, Currency currency, int year, int month) {
        super(wb);
        this.currency = currency;
        this.year = year;
        this.month = month;
        weeks = generateWeeks(year, month);
        this.corporationMap = corporationMap;
    }

    public void fillData(Collection<Balancelike> allBalances) {
        this.balances = allBalances.stream()
                .parallel()
                .filter(it -> corporationMap.containsValue(it.getCorporation()) && it.getCurrency().equals(currency))
                .collect(Collectors.toList());
        if (J.isEmpty(corporationMap) || J.isEmpty(balances)) {
            return;
        }

        String sheetName = "合并" + currency.getCode();
        sheet = wb.createSheet(sheetName);
        int rowIndex = fillHeadData();
        int weekIndex = 1;
        for (Collection<LocalDate> lds : weeks) {
            BalanceSumWeekData weekData = new BalanceSumWeekData(this, weekIndex++, lds, balances);
            rowIndex = weekData.fillData(rowIndex);
        }

    }

    private int fillHeadData() {
        sheet.createFreezePane(2, 3);

        Cell cell = cell(0, 0);
        cell.setCellValue(year + "年" + month + "月合并" + "[" + currency.getName() + "]");
        cssHead1C.accept(cell);
        head1Region(0, 0, 0, 8);
        head1Region(1, 2, 0, 1);

        for (int i = 0; i < WEEK_NAMES.length; i++) {
            final int cellIndex = i + 2;
            cell = cell(1, cellIndex);
            cell.setCellValue(WEEK_NAMES[i]);
            cssSumHead2C.accept(cell);

            cell = cell(2, cellIndex);
            cell.setCellValue("金额");
            cssHead3C.accept(cell);
        }
        return 3;
    }

    public Set<Corporation> getCorporations(BalanceSumType sumType) {
        return Arrays.stream(sumType.getCorporationIds())
                .filter(Objects::nonNull)
                .filter(corporationMap::containsKey)
                .map(corporationMap::get)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toSet());
    }

    @Override
    public int compareTo(@NotNull BalanceSumSheetData o) {
        return ComparisonChain.start()
                .compare(currency, o.currency)
                .result();
    }
}
