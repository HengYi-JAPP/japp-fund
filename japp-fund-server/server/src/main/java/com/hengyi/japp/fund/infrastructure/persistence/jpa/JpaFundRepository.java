package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.Fund;
import com.hengyi.japp.fund.domain.repository.FundRepository;
import org.jzb.J;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaFundRepository extends JpaCURDRepository<Fund, String> implements FundRepository, Serializable {

    @Override
    public Stream<Fund> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) {
        return em.createNamedQuery("Fund.queryByCorpCurDate", Fund.class)
                .setParameter("corporations", corporations)
                .setParameter("currencies", currencies)
                .setParameter("sDate", J.date(ldStart))
                .setParameter("eDate", J.date(ldEnd))
                .getResultList()
                .stream();
    }

}
