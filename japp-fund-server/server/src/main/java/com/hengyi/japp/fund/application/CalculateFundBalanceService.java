package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.domain.Fund;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * Created by jzb on 17-7-13.
 */
public interface CalculateFundBalanceService {
    void reCalculateByCreate(Fund fund);

    void reCalculateByUpdate(Fund fund, Date oldDate, BigDecimal oldMoney);

    void reCalculateByDelete(Fund fund);

    void reCalculateByDate(Set<String> corporationIds, LocalDate ldStart, LocalDate ldEnd);
}
