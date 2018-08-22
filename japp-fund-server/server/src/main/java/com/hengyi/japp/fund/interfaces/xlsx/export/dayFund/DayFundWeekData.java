package com.hengyi.japp.fund.interfaces.xlsx.export.dayFund;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.*;
import static java.util.stream.Collectors.toMap;


public class DayFundWeekData extends BaseFillData {
    private final int weekIndex;

    private final Map<LocalDate, DayFundDayData> dayMap;
    private Row dateRow;
    private Row prevBalanceRow;
    private Row lackBalanceRow;
    private Row todayBalanceRow;

    public DayFundWeekData(Sheet sheet, int weekIndex, Collection<LocalDate> lds, Collection<Balancelike> balances, Collection<Fundlike> funds) {
        super(sheet);
        this.weekIndex = weekIndex;
        this.dayMap = lds.stream().parallel()
                .map(ld -> new DayFundDayData(sheet, ld, balances, funds))
                .collect(toMap(DayFundDayData::getLd, Function.identity()));
    }

    public int fillData(final int rowStartIndex) {
        dateRow = row(rowStartIndex);
        Cell cell = cell(dateRow, 0);
        cell.setCellValue("第" + weekIndex + "周");
        cell = cell(dateRow, 1);
        cell.setCellValue("日期");

        prevBalanceRow = row(dateRow.getRowNum() + 1);
        cell = cell(prevBalanceRow, 1);
        cell.setCellValue("上日余额");

        lackBalanceRow = row(prevBalanceRow.getRowNum() + getOutFundRows() + 2);
        cell = cell(lackBalanceRow, 1);
        cell.setCellValue("资金余额缺");

        todayBalanceRow = row(lackBalanceRow.getRowNum() + getInFundRows() + 2);
        cell = cell(todayBalanceRow, 1);
        cell.setCellValue("本日余额");

        IntStream.range(prevBalanceRow.getRowNum() + 1, lackBalanceRow.getRowNum()).forEach(rId ->
                cell(rId, 1).setCellValue("支出(" + (rId - prevBalanceRow.getRowNum()) + ")")
        );
        IntStream.range(lackBalanceRow.getRowNum() + 1, todayBalanceRow.getRowNum()).forEach(rId ->
                cell(rId, 1).setCellValue("收入(" + (rId - lackBalanceRow.getRowNum()) + ")")
        );

        dayMap.values().forEach(dayData -> dayData.fillData(dateRow, prevBalanceRow, lackBalanceRow, todayBalanceRow));

        setStyle(rowStartIndex);
        return todayBalanceRow.getRowNum() + 1;
    }

    public int getOutFundRows() {
        return dayMap.values().stream()
                .parallel()
                .map(DayFundDayData::getOutFunds)
                .filter(J::nonEmpty)
                .mapToInt(Collection::size)
                .max()
                .orElse(0);
    }

    public int getInFundRows() {
        return dayMap.values().stream()
                .parallel()
                .map(DayFundDayData::getInFunds)
                .filter(J::nonEmpty)
                .mapToInt(Collection::size)
                .max()
                .orElse(0);
    }

    private void setStyle(int rowStartIndex) {
        IntStream.range(rowStartIndex, todayBalanceRow.getRowNum() + 1)
                .mapToObj(this::row)
                .flatMap(row -> IntStream.range(0, 16)
                        .mapToObj(cId -> cell(row, cId))
                )
                .forEach(cssDefaultC);

        cssWeekC.accept(cell(rowStartIndex, 0));
        weekIndexRegion(rowStartIndex, todayBalanceRow.getRowNum(), 0, 0);

        IntStream.range(1, 8).forEach(i -> {
            final int colIndex = i * 2;
            cssDateC.accept(cell(dateRow, colIndex));
            dateRegion(dateRow.getRowNum(), dateRow.getRowNum(), colIndex, colIndex + 1);
        });

        IntStream.range(1, 16).forEach(cId -> {
            cssPrevBalanceC.accept(cell(prevBalanceRow, cId));
            cssLackBalanceC.accept(cell(lackBalanceRow, cId));
            cssTodayBalanceC.accept(cell(todayBalanceRow, cId));
        });
    }

}
