package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

public interface BalancelikeService {
    /**
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @param ldDivide     取计划的日期，代表从这天起的余额，需要通过结合 日计划 重新算
     * @return
     * @throws Exception
     */
    Stream<? extends Balancelike> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd, LocalDate ldDivide) throws Exception;

}
