package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.share.CURDEntityRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-20.
 */
public interface FundBalanceRepository extends CURDEntityRepository<FundBalance, String> {
    @Override
    FundBalance save(FundBalance fundBalance);

    FundBalance find(Corporation corporation, Currency currency, Date date);

    FundBalance find(Corporation corporation, Currency currency, LocalDate ld);

    FundBalance findOrMaxNear(Corporation corporation, Currency currency, Date date);

    void addBalance(Corporation corporation, Currency currency, Date date, BigDecimal money);

    Stream<FundBalance> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception;

}
