package com.hengyi.japp.fund.interfaces.xlsx.export;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Fundlike;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;

import java.math.BigDecimal;
import java.util.Optional;

import static com.hengyi.japp.fund.interfaces.xlsx.PoiUtil.MONEY_DIVISOR;
import static org.apache.poi.ss.util.CellUtil.*;

public abstract class BaseFillData {
    protected final Workbook wb;
    protected Sheet sheet;

    protected BaseFillData(Workbook wb) {
        this.wb = wb;
    }

    protected BaseFillData(Sheet sheet) {
        this(sheet.getWorkbook());
        this.sheet = sheet;
    }

    protected Row row(int i) {
        return getRow(i, sheet);
    }

    protected Cell cell(Row row, int cId) {
        return getCell(row, cId);
    }

    protected Cell cell(int rId, int cId) {
        return cell(row(rId), cId);
    }

    protected void money(Cell cell, double v) {
        v = v / MONEY_DIVISOR;
        cell.setCellValue(v);

        DataFormat df = wb.createDataFormat();
//        CellUtil.setCellStyleProperty(cell, DATA_FORMAT, df.getFormat("0"));
        CellUtil.setCellStyleProperty(cell, DATA_FORMAT, df.getFormat("#,##0"));
    }

    protected void money(Cell cell, BigDecimal v) {
        Optional.ofNullable(v)
                .map(BigDecimal::doubleValue)
                .ifPresent(it -> money(cell, it));
    }

    protected void money(Cell cell, Balancelike v) {
        Optional.ofNullable(v)
                .map(Balancelike::getBalance)
                .ifPresent(it -> money(cell, it));
    }

    protected void money(Cell cell, Fundlike v) {
        Optional.ofNullable(v)
                .map(Fundlike::getMoney)
                .ifPresent(it -> money(cell, it));
    }

    //抬头
    protected CellRangeAddress head1Region(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        sheet.addMergedRegion(region);
        return region;
    }

    // 周几 第2行
    protected CellRangeAddress weekRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        sheet.addMergedRegion(region);
        return region;
    }

    // 第几周 第1列
    protected CellRangeAddress weekIndexRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
        sheet.addMergedRegion(region);
        return region;
    }

    // 几号 日期
    protected CellRangeAddress dateRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderBottom(BorderStyle.DOTTED, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.DOTTED, region, sheet);
        sheet.addMergedRegion(region);
        return region;
    }

    public Sheet getSheet() {
        return sheet;
    }
}
