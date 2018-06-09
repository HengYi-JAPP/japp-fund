package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.MonthFundPlan;
import com.hengyi.japp.fund.domain.repository.MonthFundPlanRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-26.
 */
@ApplicationScoped
public class JpaMonthFundPlanRepository extends JpaCURDRepository<MonthFundPlan, String> implements MonthFundPlanRepository, Serializable {
    @Override
    public Stream<MonthFundPlan> query(Set<Corporation> corporations, Set<Currency> currencies, int year) {
        return em.createNamedQuery("MonthFundPlan.queryByCorpCurYear", MonthFundPlan.class)
                .setParameter("corporations", corporations)
                .setParameter("currencies", currencies)
                .setParameter("year", year)
                .getResultList()
                .stream();
    }

    @Override
    public Stream<MonthFundPlan> query(Set<Corporation> corporations, Set<Currency> currencies, int year, int month) {
        return em.createNamedQuery("MonthFundPlan.queryByCorpCurYearMonth", MonthFundPlan.class)
                .setParameter("corporations", corporations)
                .setParameter("currencies", currencies)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList()
                .stream();
    }

}
