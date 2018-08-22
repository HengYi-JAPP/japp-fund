package com.hengyi.japp.fund.interfaces.xlsx;

import com.github.ixtf.japp.core.J;
import com.github.ixtf.japp.ee.Jee;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.domain.DayFundPlan;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by jzb on 16-12-9.
 */
public abstract class DayFundPlanExcelImport implements Callable<Collection<DayFundPlan>> {
    protected final FundlikeService fundlikeService;
    protected final File file;

    protected DayFundPlanExcelImport(File file) {
        this.fundlikeService = Jee.getBean(FundlikeService.class);
        this.file = file;
    }

    @Override
    public Collection<DayFundPlan> call() throws Exception {
        Objects.requireNonNull(file);
        Validate.isTrue(file.exists());
        try (FileInputStream fis = new FileInputStream(file)) {
            String ext = FilenameUtils.getExtension(file.getName());
            if (J.equalsIgnoreCase("xlsx", ext)) {
                return handleWorkBook(new XSSFWorkbook(fis));
            } else if (J.equalsIgnoreCase("xls", ext)) {
                return handleWorkBook(new HSSFWorkbook(fis));
            }
            throw new RuntimeException(ext + " 格式错误！");
        }
    }

    protected abstract Collection<DayFundPlan> handleWorkBook(Workbook wb);
}
