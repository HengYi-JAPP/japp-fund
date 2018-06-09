package com.hengyi.japp.fund.interfaces.fi;

import com.hengyi.japp.fund.domain.Balancelike;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.interfaces.fi.domain.AccountFund;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-12-3.
 */
public interface FiService {
    /**
     * 真实余额截止到当天
     * 如果查当天后的余额，会结合本地的 日计划 计算后续余额
     *
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @return
     * @throws Exception
     */
    Stream<? extends Balancelike> queryBalance(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception;

    Stream<AccountFund> queryFund(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception;

    Collection<CorporationSuggest> suggestCorporations(String q) throws Exception;

    CorporationSuggest findCorporationSuggest(long id) throws Exception;

    Collection<CurrencySuggest> suggestCurrencies(String q) throws Exception;

    CurrencySuggest findCurrencySuggest(String code) throws Exception;


}
