package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.share.CURDEntityRepository;

/**
 * Created by jzb on 16-10-20.
 */
public interface CurrencyRepository extends CURDEntityRepository<Currency, String> {
    Currency findByCode(String code);
}
