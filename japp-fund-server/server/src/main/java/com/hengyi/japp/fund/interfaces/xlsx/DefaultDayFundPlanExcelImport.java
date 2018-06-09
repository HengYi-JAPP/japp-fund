package com.hengyi.japp.fund.interfaces.xlsx;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.hengyi.japp.fund.domain.DayFundPlan;
import com.hengyi.japp.fund.domain.MonthFundPlan;
import org.apache.poi.ss.usermodel.*;
import org.jzb.J;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.MONEY_DIVISOR;

/**
 * Created by jzb on 16-12-9.
 */
public class DefaultDayFundPlanExcelImport extends DayFundPlanExcelImport {
    //排除这些行
    public static final Set<String> FILTER_TEXT_SET = ImmutableSet.of("上日现金余额", "资金余额缺", "本日现金余额");
    public static final Pattern DATE_PATTERN = Pattern.compile("\\d+号$");
    public static final Function<Cell, String> PURE_TEXT_FUN = cell -> Optional.ofNullable(cell)
            .filter(c -> c.getCellTypeEnum() == CellType.STRING)
            .map(Cell::getStringCellValue)
            .map(J::deleteWhitespace)
            .orElse(null);
    public static final Predicate<Row> DATEROW_PREDICATE = row -> Optional.ofNullable(row)
            .map(r -> r.getCell(1))
            .map(PURE_TEXT_FUN)
            .map("日期"::equalsIgnoreCase)
            .orElse(false);
    public static final Predicate<Cell> DATECELL_PREDICATE = cell -> Optional.ofNullable(cell)
            .map(PURE_TEXT_FUN)
            .filter(J::nonBlank)
            .map(DATE_PATTERN::matcher)
            .map(matcher -> matcher.matches())
            .orElse(false);
    public static final Function<Cell, Optional<BigDecimal>> MONEY_FUN = cell -> Optional.ofNullable(cell)
            .filter(c -> c.getCellTypeEnum() == CellType.NUMERIC)
            .map(Cell::getNumericCellValue)
            .filter(Predicate.isEqual(0).negate())
            .map(money -> money * MONEY_DIVISOR)
            .map(BigDecimal::valueOf);
    protected final Collection<DayFundPlan> result = Lists.newArrayList();
    private final MonthFundPlan monthFundPlan;

    public DefaultDayFundPlanExcelImport(File file, MonthFundPlan monthFundPlan) {
        super(file);
        this.monthFundPlan = monthFundPlan;
    }

    public static void main(String[] args) throws Exception {
        final File file = new File("/home/jzb/fund.xls");
        MonthFundPlan monthFundPlan = new MonthFundPlan();
        monthFundPlan.setYear(2016);
        monthFundPlan.setMonth(12);
        DayFundPlanExcelImport test = new DefaultDayFundPlanExcelImport(file, monthFundPlan);
//        Collection<DayFundPlan> result = test.call();
        System.out.println(test.call().size());
    }

    @Override
    protected Collection<DayFundPlan> handleWorkBook(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        StreamSupport.stream(sheet.spliterator(), true)
                .filter(DATEROW_PREDICATE)
                .forEach(row -> IntStream.range(2, 16)
                        .parallel()
                        .mapToObj(row::getCell)
                        .filter(DATECELL_PREDICATE)
                        .forEach(this::handleDateCell)
                );
        return result;
    }

    private void handleDateCell(Cell cell) {
        final int day = Integer.valueOf(PURE_TEXT_FUN.apply(cell).replace("号", ""));
        final Date date = J.date(LocalDate.of(monthFundPlan.getYear(), monthFundPlan.getMonth(), day));
        final int columnIndex = cell.getColumnIndex();
        final Sheet sheet = cell.getSheet();
        final Predicate<Row> breakPredicate = row -> row == null
                || row.getRowNum() >= sheet.getPhysicalNumberOfRows()
                || DATECELL_PREDICATE.test(row.getCell(columnIndex));

        Row row = cell.getRow();
        Row nextRow = sheet.getRow(row.getRowNum() + 1);
        do {
            row = nextRow;
            nextRow = sheet.getRow(row.getRowNum() + 1);

            String filterText = PURE_TEXT_FUN.apply(row.getCell(1));
            if (FILTER_TEXT_SET.contains(filterText))
                continue;

            Cell moneyCell = row.getCell(columnIndex);
            Cell noteCell = row.getCell(columnIndex + 1);
            MONEY_FUN.apply(moneyCell)
                    .map(money -> {
                        String note = PURE_TEXT_FUN.apply(noteCell);
                        DayFundPlan dayFundPlan = new DayFundPlan();
                        dayFundPlan.setMonthFundPlan(monthFundPlan);
                        dayFundPlan.setDate(date);
                        dayFundPlan.setMoney(money);
                        dayFundPlan.setNote(note);
                        return dayFundPlan;
                    })
                    .ifPresent(result::add);
        } while (!breakPredicate.test(nextRow));
    }
}
