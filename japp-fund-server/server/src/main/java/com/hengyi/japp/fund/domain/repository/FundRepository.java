package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fund;
import com.hengyi.japp.fund.share.CURDEntityRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-20.
 */
public interface FundRepository extends CURDEntityRepository<Fund, String> {
    Fund save(Fund fund);

    Stream<Fund> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd);
}
