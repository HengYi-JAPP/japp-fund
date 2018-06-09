package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.domain.repository.FundBalanceRepository;
import org.jzb.J;
import org.jzb.ee.JEE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryFundBalanceTask extends RecursiveTask<Stream<FundBalance>> {
    private final FundBalanceRepository fundBalanceRepository;
    private final Set<Corporation> corporations;
    private final Set<Currency> currencies;
    private final LocalDate ldStart;
    private final LocalDate ldEnd;

    public QueryFundBalanceTask(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        fundBalanceRepository = JEE.getBean(FundBalanceRepository.class);
        this.corporations = corporations;
        this.currencies = currencies
                .stream()
                .parallel()
                // 目前只查承兑汇票
                .filter(Currency::isCdhp)
                .collect(Collectors.toSet());
        this.ldStart = ldStart;
        this.ldEnd = ldEnd;
    }

    @Override
    protected Stream<FundBalance> compute() {
        if (J.isEmpty(corporations)) {
            return Stream.empty();
        }

        return Stream.iterate(ldStart, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                .parallel()
                .flatMap(ld -> corporations.stream()
                        .parallel()
                        .flatMap(corporation -> currencies.stream()
                                .parallel()
                                .map(currency -> new Single(corporation, currency, ld))
                                .peek(Single::fork)
                                .map(Single::join)
                        )
                );
    }

    private class Single extends RecursiveTask<FundBalance> {
        private final Corporation corporation;
        private final Currency currency;
        private final Date date;

        private Single(Corporation corporation, Currency currency, LocalDate ld) {
            this.corporation = corporation;
            this.currency = currency;
            date = J.date(ld);
        }

        @Override
        protected FundBalance compute() {
            FundBalance result = new FundBalance();
            result.setCorporation(corporation);
            result.setCurrency(currency);
            result.setDate(date);
            BigDecimal balance = Optional.ofNullable(fundBalanceRepository.findOrMaxNear(corporation, currency, date))
                    .map(FundBalance::getBalance)
                    .orElse(BigDecimal.ZERO);
            result.setBalance(balance);
            return result;
        }
    }

}
