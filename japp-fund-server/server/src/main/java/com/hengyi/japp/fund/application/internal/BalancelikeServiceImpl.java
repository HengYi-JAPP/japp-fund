package com.hengyi.japp.fund.application.internal;

import com.hengyi.japp.fund.application.BalancelikeService;
import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.domain.*;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;
import com.hengyi.japp.fund.domain.repository.FundBalanceRepository;
import com.hengyi.japp.fund.interfaces.fi.FiService;
import org.apache.commons.lang3.tuple.Triple;
import org.jzb.J;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hengyi.japp.fund.Constant.CDHP;

@Stateless
public class BalancelikeServiceImpl implements BalancelikeService {
    @Inject
    private FiService fiService;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private FundBalanceRepository fundBalanceRepository;
    @Inject
    private FundlikeService fundlikeService;

    @Override
    public Stream<? extends Balancelike> query(Set<Corporation> corporations, Set<Currency> currencies, final LocalDate ldStart, final LocalDate ldEnd, final LocalDate ldDivide) throws Exception {
        if (!ldDivide.isAfter(ldStart)) {//全部为计算余额
            final LocalDate ldCalc = ldDivide.plusDays(-1);
            Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> calcBalanceMap = query(corporations, currencies, ldCalc, ldCalc)
                    .parallel()
                    .collect(Collectors.toMap(Balancelike::triple, Balancelike::getBalance));
            Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> sumByDayMap = fundlikeService.queryLocal(corporations, currencies, ldDivide, ldEnd)
                    .parallel()
                    .collect(Collectors.toMap(Fundlike::triple, Fundlike::getMoney, (a, b) -> a.add(b)));
            return Stream.iterate(ldStart, it -> it.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(ldStart, ldEnd) + 1)
                    .parallel()
                    .flatMap(ld -> corporations.stream()
                            .parallel()
                            .flatMap(corporation -> currencies.stream()
                                    .parallel()
                                    .map(currency -> getBalance(corporation, currency, ld, ldCalc, calcBalanceMap, sumByDayMap))
                            )
                    );
        } else if (ldDivide.isAfter(ldEnd)) {//全部为实际余额
            return query(corporations, currencies, ldStart, ldEnd);
        } else {
            return Stream.concat(
                    query(corporations, currencies, ldStart, ldDivide.plusDays(-1), ldDivide),
                    query(corporations, currencies, ldDivide, ldEnd, ldDivide)
            );
        }
    }

    private Stream<? extends Balancelike> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception {
        Stream<Balancelike> stream = Stream.empty();
        Stream<? extends Balancelike> streamFi = fiService.queryBalance(corporations, currencies, ldStart, ldEnd);
        stream = Stream.concat(stream, streamFi);
        Currency cdhp = currencyRepository.findByCode(CDHP);
        if (currencies.contains(cdhp)) {
            Stream<FundBalance> streamCdhp = fundBalanceRepository.query(corporations, currencies, ldStart, ldEnd);
            stream = Stream.concat(stream, streamCdhp);
        }
        return stream;
    }

    private Balancelike getBalance(Corporation corporation, Currency currency, final LocalDate ld, final LocalDate ldCalc, Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> calcBalanceMap, Map<Triple<Corporation, Currency, LocalDate>, BigDecimal> sumByDayMap) {
        FundBalance result = new FundBalance();
        result.setCorporation(corporation);
        result.setCurrency(currency);
        result.setDate(J.date(ld));

        Triple<Corporation, Currency, LocalDate> calcKey = Triple.of(corporation, currency, ldCalc);
        BigDecimal balance = Optional.ofNullable(calcBalanceMap.get(calcKey))
                .map(calcBalance -> {
                    double sum = sumByDayMap.entrySet()
                            .stream()
                            .parallel()
                            .filter(entry -> {
                                Triple<Corporation, Currency, LocalDate> sumDayKey = entry.getKey();
                                LocalDate sumDayLd = sumDayKey.getRight();
                                return corporation.equals(sumDayKey.getLeft()) &&
                                        currency.equals(sumDayKey.getMiddle()) &&
                                        !sumDayLd.isAfter(ld);
                            })
                            .map(Map.Entry::getValue)
                            .mapToDouble(BigDecimal::doubleValue)
                            .sum();
                    return calcBalance.add(BigDecimal.valueOf(sum));
                })
                .orElse(BigDecimal.ZERO);
        result.setBalance(balance);
        return result;
    }

}
