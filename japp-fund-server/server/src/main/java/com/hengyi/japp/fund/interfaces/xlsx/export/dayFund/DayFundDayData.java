package com.hengyi.japp.fund.interfaces.xlsx.export.dayFund;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Fundlike;
import com.hengyi.japp.fund.interfaces.xlsx.export.BaseFillData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.cssNoteC;
import static java.util.stream.Collectors.groupingBy;

public class DayFundDayData extends BaseFillData implements Comparable<DayFundDayData> {
    private final int colIndex;
    private final LocalDate ld;
    private final Optional<Balancelike> prevBalance;
    private final Balancelike balance;
    private final Collection<Fundlike> outFunds;
    private final Collection<Fundlike> inFunds;

    DayFundDayData(Sheet sheet, LocalDate ld, Collection<Balancelike> balances, Collection<Fundlike> funds) {
        super(sheet);
        this.ld = ld;
        this.colIndex = ld.getDayOfWeek().getValue() * 2;
        this.prevBalance = balances.stream()
                .parallel()
                .filter(it -> ld.plusDays(-1).isEqual(it.localDate()))
                .findFirst();
        this.balance = balances.stream()
                .parallel()
                .filter(it -> ld.isEqual(it.localDate()))
                .findFirst()
                .get();

        Map<Integer, List<Fundlike>> map = funds.stream()
                .parallel()
                .filter(it -> ld.isEqual(it.localDate()))
                .collect(groupingBy(fund -> fund.getMoney().doubleValue() < 0 ? -1 : 1));
        this.outFunds = map.getOrDefault(-1, Collections.EMPTY_LIST);
        this.inFunds = map.getOrDefault(1, Collections.EMPTY_LIST);
    }

    public void fillData(final Row dateRow, final Row prevBalanceRow, final Row lackBalanceRow, final Row todayBalanceRow) {
        Cell cell = cell(dateRow, colIndex);
        cell.setCellValue(ld.getDayOfMonth() + "号");

        prevBalance.ifPresent(v -> money(cell(prevBalanceRow, colIndex), v));
        fillFundData(prevBalanceRow.getRowNum() + 1, outFunds);

        cell = cell(lackBalanceRow, colIndex);
        money(cell, getLackBalance());
        fillFundData(lackBalanceRow.getRowNum() + 1, inFunds);

        cell = cell(todayBalanceRow, colIndex);
        money(cell, balance);
    }

    private void fillFundData(int rowIndex, Collection<Fundlike> funds) {
        for (Fundlike fund : funds) {
            Row row = row(rowIndex++);
            Cell cell = cell(row, colIndex);
            money(cell, fund);

            cell = cell(row, colIndex + 1);
            cell.setCellValue(fund.getNote());
            cssNoteC.accept(cell);
        }
    }

    private double getLackBalance() {
        Stream<BigDecimal> outStream = outFunds
                .stream()
                .parallel()
                .map(Fundlike::getMoney);
        return prevBalance
                .map(Balancelike::getBalance)
                .map(Stream::of)
                .map(it -> Stream.concat(it, outStream))
                .orElse(outStream)
                .parallel()
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }

    public LocalDate getLd() {
        return ld;
    }

    Collection<Fundlike> getOutFunds() {
        return outFunds;
    }

    Collection<Fundlike> getInFunds() {
        return inFunds;
    }

    @Override
    public int compareTo(@NotNull DayFundDayData o) {
        return this.ld.compareTo(o.ld);
    }
}
