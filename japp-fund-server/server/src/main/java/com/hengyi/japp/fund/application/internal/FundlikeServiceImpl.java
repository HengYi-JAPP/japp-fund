package com.hengyi.japp.fund.application.internal;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.application.command.BatchFundlikeUpdateCommand;
import com.hengyi.japp.fund.application.command.FundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.domain.repository.DayFundPlanRepository;
import com.hengyi.japp.fund.domain.repository.FundRepository;
import com.hengyi.japp.fund.domain.repository.MonthFundPlanRepository;
import com.hengyi.japp.fund.interfaces.fi.FiService;
import com.hengyi.japp.fund.interfaces.fi.domain.AccountFund;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.Constant.CDHP;

@Stateless
public class FundlikeServiceImpl implements FundlikeService {
    @Inject
    private FundService fundService;
    @Inject
    private DayFundPlanService dayFundPlanService;
    @Inject
    private FiService fiService;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private MonthFundPlanRepository monthFundPlanRepository;
    @Inject
    private DayFundPlanRepository dayFundPlanRepository;
    @Inject
    private FundRepository fundRepository;

    @Override
    public Collection<? extends AbstractFundlikeEntity> create(Principal principal, String monthFundPlanId, BatchFundlikeUpdateCommand command) throws Exception {
        LocalDate ldStart = J.localDate(command.getStartDate());
        LocalDate ldEnd = J.localDate(command.getEndDate());
        return Stream.iterate(ldStart, it -> it.plusDays(1))
                .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                .map(J::date)
                .map(date -> {
                    FundlikeUpdateCommand _command = new FundlikeUpdateCommand();
                    _command.setDate(date);
                    _command.setMoney(command.getMoney());
                    _command.setNote(command.getNote());
                    try {
                        return create(principal, monthFundPlanId, _command);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public AbstractFundlikeEntity create(Principal principal, String monthFundPlanId, FundlikeUpdateCommand command) throws Exception {
        MonthFundPlan monthFundPlan = monthFundPlanRepository.find(monthFundPlanId);
        Currency currency = monthFundPlan.getCurrency();
        if (currency.isCdhp()) {
            return fundService.create(principal, monthFundPlanId, command);
        }
        return dayFundPlanService.create(principal, monthFundPlanId, command);
    }

    @Override
    public AbstractFundlikeEntity update(Principal principal, String monthFundPlanId, String id, FundlikeUpdateCommand command) throws Exception {
        MonthFundPlan monthFundPlan = monthFundPlanRepository.find(monthFundPlanId);
        Currency currency = monthFundPlan.getCurrency();
        if (currency.isCdhp()) {
            return fundService.update(principal, monthFundPlanId, id, command);
        }
        return dayFundPlanService.update(principal, monthFundPlanId, id, command);
    }

    @Override
    public AbstractFundlikeEntity find(String id) {
        AbstractFundlikeEntity fundlike = dayFundPlanRepository.find(id);
        return Optional.ofNullable(fundlike)
                .orElseGet(() -> fundRepository.find(id));
    }

    @Override
    public void delete(Principal principal, String id) throws Exception {
        DayFundPlan dayFundPlan = dayFundPlanRepository.find(id);
        if (dayFundPlan != null) {
            dayFundPlanService.delete(principal, id);
            return;
        }
        fundService.delete(principal, id);
    }

    @Override
    public Stream<? extends Fundlike> query(Set<Corporation> corporations, Set<Currency> currencies, final LocalDate ldStart, final LocalDate ldEnd, final LocalDate ldDivide) throws Exception {
        if (!ldDivide.isAfter(ldStart)) {//全部为计算,取本地
            return queryLocal(corporations, currencies, ldStart, ldEnd);
        } else if (ldDivide.isAfter(ldEnd)) {//全部为实际余额
            return queryFi(corporations, currencies, ldStart, ldEnd);
        } else {
            return Stream.concat(
                    query(corporations, currencies, ldStart, ldDivide.plusDays(-1), ldDivide),
                    query(corporations, currencies, ldDivide, ldEnd, ldDivide)
            );
        }
    }

    /**
     * 只查询本地 日计划，
     * 但承兑汇票查的是本地 日执行
     *
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @return
     * @throws Exception
     */
    @Override
    public Stream<? extends AbstractFundlikeEntity> queryLocal(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        Stream<? extends AbstractFundlikeEntity> stream = Stream.empty();
        Stream<DayFundPlan> streamPlan = dayFundPlanRepository.query(corporations, currencies, ldStart, ldEnd);
        stream = Stream.concat(stream, streamPlan);
        Currency cdhp = currencyRepository.findByCode(CDHP);
        if (currencies.contains(cdhp)) {
            Stream<Fund> streamFund = fundRepository.query(corporations, currencies, ldStart, ldEnd);
            stream = Stream.concat(stream, streamFund);
        }
        return stream;
    }

    /**
     * 查询资金系统，日执行
     * 由于资金系统是实时的银行流水，所以当天的数据是 日执行，大于当天的数据为 本地日计划
     * 承兑汇票查的是本地 日执行
     *
     * @param corporations
     * @param currencies
     * @param ldStart
     * @param ldEnd
     * @return
     * @throws Exception
     */
    private Stream<? extends Fundlike> queryFi(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception {
        Stream<? extends Fundlike> stream = Stream.empty();
        Stream<AccountFund> streamFi = fiService.queryFund(corporations, currencies, ldStart, ldEnd);
        stream = Stream.concat(stream, streamFi);

        LocalDate ldCur = LocalDate.now();
        if (ldEnd.isAfter(ldCur)) {
            LocalDate ld = ldStart.isAfter(ldCur) ? ldStart : ldCur.plusDays(1);
            Stream<DayFundPlan> streamDayFundPlan = dayFundPlanRepository.query(corporations, currencies, ld, ldEnd);
            stream = Stream.concat(stream, streamDayFundPlan);
        }

        Currency cdhp = currencyRepository.findByCode(CDHP);
        if (currencies.contains(cdhp)) {
            Stream<Fund> streamLocal = fundRepository.query(corporations, currencies, ldStart, ldEnd);
            stream = Stream.concat(stream, streamLocal);
        }
        return stream;
    }

}
