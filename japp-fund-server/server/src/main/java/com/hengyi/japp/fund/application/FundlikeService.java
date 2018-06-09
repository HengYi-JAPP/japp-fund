package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.application.command.BatchFundlikeUpdateCommand;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.AbstractFundlikeEntity;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fundlike;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public interface FundlikeService {

    Collection<? extends AbstractFundlikeEntity> create(Principal principal, String monthFundPlanId, BatchFundlikeUpdateCommand command) throws Exception;

    <T extends AbstractFundlikeEntity> T create(Principal principal, String monthFundPlanId, FundlikeUpdateCommand command) throws Exception;

    <T extends AbstractFundlikeEntity> T update(Principal principal, String monthFundPlanId, String id, FundlikeUpdateCommand command) throws Exception;

    <T extends AbstractFundlikeEntity> T find(String id);

    void delete(Principal principal, String id) throws Exception;

    /**
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @param ldDivide
     * @return
     * @throws Exception
     */
    Stream<? extends Fundlike> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd, LocalDate ldDivide) throws Exception;

    Stream<? extends AbstractFundlikeEntity> queryLocal(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception;
}
