package com.hengyi.japp.fund.application.internal;

import com.github.ixtf.japp.core.J;
import com.google.common.collect.Sets;
import com.hengyi.japp.fund.application.CalculateFundBalanceService;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fund;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.domain.repository.CorporationRepository;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.domain.repository.FundBalanceRepository;
import com.hengyi.japp.fund.domain.repository.FundRepository;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.Constant.CDHP;
import static com.hengyi.japp.fund.infrastructure.persistence.jpa.JpaFundBalanceRepository.INIT_LD;

/**
 * Created by jzb on 16-10-26.
 * 余额计算，单线程
 */
@Startup
@Singleton
@AccessTimeout(value = 1, unit = TimeUnit.HOURS)
public class CalculateFundBalanceServiceImpl implements CalculateFundBalanceService {
    @Inject
    private FundBalanceRepository fundBalanceRepository;
    @Inject
    private CorporationRepository corporationRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private FundRepository fundRepository;

    @Override
    public void reCalculateByCreate(Fund fund) {
        final Corporation corporation = fund.getCorporation();
        final Currency currency = fund.getCurrency();
        final Date date = fund.getDate();
        getOrCreate(corporation, currency, date);
        fundBalanceRepository.addBalance(corporation, currency, date, fund.getMoney());
    }

    private FundBalance getOrCreate(final Corporation corporation, final Currency currency, final Date date) {
        FundBalance fundBalance = fundBalanceRepository.find(corporation, currency, date);
        if (fundBalance != null) {
            return fundBalance;
        }
        if (J.localDate(date).isBefore(INIT_LD)) {
            throw new RuntimeException();
        }

        fundBalance = new FundBalance();
        fundBalance.setCorporation(corporation);
        fundBalance.setCurrency(currency);
        fundBalance.setDate(date);
        FundBalance preFundBalance = getOrCreate(corporation, currency, DateUtils.addDays(date, -1));
        fundBalance.setBalance(preFundBalance.getBalance());
        return fundBalanceRepository.save(fundBalance);
    }

    @Override
    public void reCalculateByUpdate(final Fund fund, final Date oldDate, final BigDecimal oldMoney) {
        fundBalanceRepository.addBalance(fund.getCorporation(), fund.getCurrency(), oldDate, oldMoney.negate());
        reCalculateByCreate(fund);
    }

    @Override
    public void reCalculateByDelete(final Fund fund) {
        fundBalanceRepository.addBalance(fund.getCorporation(), fund.getCurrency(), fund.getDate(), fund.getMoney().negate());
    }

    @Override
    public void reCalculateByDate(Set<String> corporationIds, final LocalDate ldStart, final LocalDate ldEnd) {
        Currency currency = currencyRepository.findByCode(CDHP);
        J.emptyIfNull(corporationIds)
                .stream()
                .map(corporationRepository::find)
                .forEach(corporation -> Stream.iterate(ldStart, it -> it.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                        .forEach(ld -> reCalculateByDate(corporation, currency, ld))
                );
    }

    private void reCalculateByDate(final Corporation corporation, final Currency currency, final LocalDate ld) {
        FundBalance preFundBalance = fundBalanceRepository.find(corporation, currency, ld.plusDays(-1));
        FundBalance fundBalance = fundBalanceRepository.find(corporation, currency, ld);
        if (fundBalance == null) {
            fundBalance = new FundBalance();
            fundBalance.setCorporation(corporation);
            fundBalance.setCurrency(currency);
            fundBalance.setDate(J.date(ld));
        }
        double sum = fundRepository.query(Sets.newHashSet(corporation), Sets.newHashSet(currency), ld, ld)
                .map(Fund::getMoney)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        double balance = preFundBalance.getBalance().doubleValue() + sum;
        fundBalance.setBalance(BigDecimal.valueOf(balance));
        fundBalanceRepository.save(fundBalance);
    }

}
