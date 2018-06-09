package com.hengyi.japp.fund.interfaces.xlsx;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;
import org.jzb.J;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import static org.apache.poi.ss.util.CellUtil.*;

public class PoiUtil {
    //万元为单位
    public static final double MONEY_DIVISOR = 10000;
    public static String[] WEEK_NAMES = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    //抬头 第1行
    public static Consumer<Cell> cssHead1C = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                Font font = c.getSheet().getWorkbook().createFont();
                font.setBold(true);
                builder.put(FONT, font.getIndex());
                setCellStyleProperties(cell, builder.build());
            });
    //抬头 第2行
    public static Consumer<Cell> cssHead2C = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                setCellStyleProperties(cell, builder.build());
            });
    public static Consumer<Cell> cssSumHead2C = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                builder.put(BORDER_BOTTOM, BorderStyle.THIN);
                builder.put(BORDER_RIGHT, BorderStyle.THIN);
                setCellStyleProperties(cell, builder.build());
            });
    //抬头 第3行
    public static Consumer<Cell> cssHead3C = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                builder.put(BORDER_BOTTOM, BorderStyle.MEDIUM);
                builder.put(BORDER_RIGHT, BorderStyle.THIN);
                setCellStyleProperties(cell, builder.build());
            });
    //第一列 第几周
    public static Consumer<Cell> cssWeekC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                builder.put(VERTICAL_ALIGNMENT, VerticalAlignment.CENTER);
                setCellStyleProperties(cell, builder.build());
            });
    //上日余额
    public static Consumer<Cell> cssPrevBalanceC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(BORDER_RIGHT, BorderStyle.DOTTED);
                builder.put(BORDER_BOTTOM, BorderStyle.DOTTED);
                builder.put(FILL_FOREGROUND_COLOR, IndexedColors.YELLOW.index);
                builder.put(FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
                setCellStyleProperties(cell, builder.build());
            });
    //资金余额缺
    public static Consumer<Cell> cssLackBalanceC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(BORDER_RIGHT, BorderStyle.DOTTED);
                builder.put(BORDER_BOTTOM, BorderStyle.DOTTED);
                builder.put(FILL_FOREGROUND_COLOR, IndexedColors.ORANGE.index);
                builder.put(FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
                setCellStyleProperties(cell, builder.build());
            });
    //本日余额
    public static Consumer<Cell> cssTodayBalanceC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(BORDER_RIGHT, BorderStyle.DOTTED);
                builder.put(BORDER_BOTTOM, BorderStyle.MEDIUM);
                builder.put(FILL_FOREGROUND_COLOR, IndexedColors.AQUA.index);
                builder.put(FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
                setCellStyleProperties(cell, builder.build());
            });
    public static Consumer<Cell> cssDateC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(ALIGNMENT, HorizontalAlignment.CENTER);
                builder.put(BORDER_RIGHT, BorderStyle.DOTTED);
                builder.put(BORDER_BOTTOM, BorderStyle.DOTTED);
                setCellStyleProperties(cell, builder.build());
            });
    public static Consumer<Cell> cssNoteC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                Sheet sheet = cell.getSheet();
                Workbook wb = sheet.getWorkbook();
                String note = cell.getStringCellValue();
                if (J.length(note) < 9) {
                    return;
                }
                String shortNote = note.substring(0, 8);
                cell.setCellValue(shortNote);

                CreationHelper factory = wb.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = factory.createClientAnchor();
                Comment comment = drawing.createCellComment(anchor);
                RichTextString str = factory.createRichTextString(note);
                comment.setString(str);
                cell.setCellComment(comment);
            });
    public static Consumer<Cell> cssDefaultC = cell -> Optional.ofNullable(cell)
            .ifPresent(c -> {
                ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                builder.put(BORDER_BOTTOM, BorderStyle.DOTTED);
                builder.put(BORDER_RIGHT, BorderStyle.DOTTED);
                setCellStyleProperties(cell, builder.build());
            });

    public static Collection<Collection<LocalDate>> generateWeeks(int year, int month) {
        LocalDate ldStart = LocalDate.of(year, month, 1);
        LocalDate ldEnd = ldStart.plusMonths(1).plusDays(-1);
        Collection<Collection<LocalDate>> weeks = Lists.newArrayList();
        Collection<LocalDate> lds = Lists.newArrayList();
        for (LocalDate ld = ldStart; ld.isBefore(ldEnd) || ld.isEqual(ldEnd); ld = ld.plusDays(1)) {
            lds.add(ld);
            if (ld.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weeks.add(ImmutableList.copyOf(lds));
                lds.clear();
            }
        }
        if (J.nonEmpty(lds)) {
            weeks.add(ImmutableList.copyOf(lds));
        }
        return ImmutableList.copyOf(weeks);
    }

}
