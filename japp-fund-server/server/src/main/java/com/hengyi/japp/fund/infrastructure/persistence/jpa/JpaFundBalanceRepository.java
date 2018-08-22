package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.FundBalance;
import com.hengyi.japp.fund.domain.repository.FundBalanceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaFundBalanceRepository extends JpaCURDRepository<FundBalance, String> implements FundBalanceRepository, Serializable {
    public static final LocalDate INIT_LD = LocalDate.of(2017, 8, 31);

    @Override
    public FundBalance save(FundBalance entity) {
        if (entity.isNew()) {
            String id = FundBalance.generateId(entity.getCorporation(), entity.getCurrency(), entity.getDate());
            entity.setId(id);
        }
        return super.save(entity);
    }

    @Override
    public FundBalance find(Corporation corporation, Currency currency, Date date) {
        String id = FundBalance.generateId(corporation, currency, date);
        return find(id);
    }

    @Override
    public FundBalance find(Corporation corporation, Currency currency, LocalDate ld) {
        return find(corporation, currency, J.date(ld));
    }

    /**
     * 查询日期小于 @param date 的最大日期的余额
     * 因为余额只有录入的那天才会生成
     *
     * @param corporation
     * @param currency
     * @param date
     * @return
     */
    @Override
    public FundBalance findOrMaxNear(Corporation corporation, Currency currency, Date date) {
        return Optional.ofNullable(find(corporation, currency, date))
                .orElseGet(() -> {
                    LocalDate ld = J.localDate(date);
                    if (ld.isBefore(INIT_LD)) {
                        return find(corporation, currency, J.date(INIT_LD));
                    }
                    // return em.createNamedQuery("FundBalance.findMaxNearByCorpCurDate", FundBalance.class)
                    //         .setParameter("corporation", corporation)
                    //         .setParameter("currency", currency)
                    //         .setParameter("date", date)
                    //         .setMaxResults(1)
                    //         .getSingleResult();
                    final TypedQuery<FundBalance> query = em.createNamedQuery("FundBalance.findMaxNearByCorpCurDate", FundBalance.class)
                            .setParameter("corporation", corporation)
                            .setParameter("currency", currency)
                            .setParameter("date", date)
                            .setMaxResults(1);
                    return Util.getSingle(query);
                });
    }

    @Override
    public Stream<FundBalance> query(Set<Corporation> corporations, Set<Currency> currencies, LocalDate ldStart, LocalDate ldEnd) throws Exception {
        QueryFundBalanceTask task = new QueryFundBalanceTask(corporations, currencies, ldStart, ldEnd);
        return ForkJoinPool.commonPool().invoke(task);
    }

    @Override
    public void addBalance(Corporation corporation, Currency currency, Date date, BigDecimal money) {
        em.createNamedQuery("FundBalance.addBalance")
                .setParameter("corporation", corporation)
                .setParameter("currency", currency)
                .setParameter("date", date)
                .setParameter("money", money)
                .executeUpdate();
    }

}
