package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.DayFundPlan;
import com.hengyi.japp.fund.domain.repository.DayFundPlanRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-26.
 */
@ApplicationScoped
public class JpaDayFundPlanRepository extends JpaCURDRepository<DayFundPlan, String> implements DayFundPlanRepository, Serializable {

    @Override
    public Stream<DayFundPlan> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        return em.createNamedQuery("DayFundPlan.queryByCorpCurDate", DayFundPlan.class)
                .setParameter("corporations", corporations)
                .setParameter("currencies", currencies)
                .setParameter("sDate", J.date(ldStart))
                .setParameter("eDate", J.date(ldEnd))
                .getResultList()
                .stream();
    }

}
