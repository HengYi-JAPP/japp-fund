package com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.Maps;
import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.*;
import static com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumType.*;
import static java.util.stream.Collectors.toMap;


public class BalanceSumWeekData extends BaseFillData {
    private final BalanceSumSheetData sheetData;
    private final int weekIndex;
    private final Map<LocalDate, BalanceSumDayData> dayMap;
    private final Map<BalanceSumType, Row> sumTypeRowMap = Maps.newLinkedHashMap();
    private Row dateRow;
    private Row todayBalanceRow;

    public BalanceSumWeekData(BalanceSumSheetData sheetData, int weekIndex, Collection<LocalDate> lds, Collection<Balancelike> balances) {
        super(sheetData.getSheet());
        this.sheetData = sheetData;
        this.weekIndex = weekIndex;
        this.dayMap = lds.stream().parallel()
                .map(ld -> new BalanceSumDayData(sheetData, ld, balances))
                .collect(toMap(BalanceSumDayData::getLd, Function.identity()));
    }

    public int fillData(final int rowStartIndex) {
        dateRow = row(rowStartIndex);
        Cell cell = cell(dateRow, 0);
        cell.setCellValue("第" + weekIndex + "周");
        cell = cell(dateRow, 1);
        cell.setCellValue("日期");

        //记录当前的小计或合计的row行数，因为，下次要从这个行开始往下写数据
        AtomicInteger tmpId = new AtomicInteger(dateRow.getRowNum() + 1);
        //注意这里的顺序，不然会对后面的数据产生影响
        Stream.of(SS_XS, SS_NB, SS, FSS_JTKG, FSS_JTCG, FSS)
                .forEach(sumType -> {
                    int rowIndex = fillCorporationsData(sumType, tmpId.get());
                    tmpId.set(rowIndex);
                });

        todayBalanceRow = row(tmpId.get());
        cell = cell(todayBalanceRow, 1);
        cell.setCellValue("本日余额");

        dayMap.values().forEach(dayData -> dayData.fillData(dateRow, sumTypeRowMap, todayBalanceRow));

        setStyle(rowStartIndex);
        return todayBalanceRow.getRowNum() + 1;
    }

    private int fillCorporationsData(BalanceSumType sumType, int rowIndex) {
        Set<Corporation> corporations = sheetData.getCorporations(sumType);
        for (Corporation corporation : corporations) {
            Row row = row(rowIndex++);
            Cell cell = cell(row, 1);
            cell.setCellValue(corporation.getName());
        }

        // check一下前面有没有该小计类型的子小计出现过，如果出现过就进行小计并显示，否则，不显示这行
        // 这里无法用该小计的计算是否为 0 来判断，必须按上下级关系，和子小计的是否出现
        boolean addFlag = J.nonEmpty(corporations) || sumTypeRowMap.keySet()
                .stream()
                .parallel()
                .map(BalanceSumType::getAllParent)
                .flatMap(Collection::stream)
                .filter(it -> it == sumType)
                .findFirst()
                .isPresent();
        if (addFlag) {
            Row row = row(rowIndex++);
            Cell cell = cell(row, 1);
            cell.setCellValue(sumType.getDisplayName());
            sumTypeRowMap.put(sumType, row);
        }
        return rowIndex;
    }

    public void setStyle(int rowStartIndex) {
        IntStream.range(rowStartIndex, todayBalanceRow.getRowNum() + 1)
                .mapToObj(this::row)
                .flatMap(row -> IntStream.range(1, 9)
                        .mapToObj(cId -> cell(row, cId))
                )
                .forEach(cssDefaultC);

        cssWeekC.accept(cell(rowStartIndex, 0));
        weekIndexRegion(rowStartIndex, todayBalanceRow.getRowNum(), 0, 0);

        IntStream.range(1, 9).forEach(cId -> {
            cssDateC.accept(cell(dateRow, cId));
            cssTodayBalanceC.accept(cell(todayBalanceRow, cId));

            sumTypeRowMap.entrySet().forEach(entry -> {
                switch (entry.getKey()) {
                    case FSS_JTKG:
                    case FSS_JTCG:
                    case SS_NB:
                    case SS_XS: {
                        cssPrevBalanceC.accept(cell(entry.getValue(), cId));
                        break;
                    }
                    case SS:
                    case FSS: {
                        cssLackBalanceC.accept(cell(entry.getValue(), cId));
                        break;
                    }
                }
            });
        });
    }
}
