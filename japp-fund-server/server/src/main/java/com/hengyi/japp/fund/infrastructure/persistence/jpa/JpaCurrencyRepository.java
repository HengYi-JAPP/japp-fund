package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.Util;
import com.hengyi.japp.fund.domain.Currency;
import com.hengyi.japp.fund.domain.repository.CurrencyRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaCurrencyRepository extends JpaCURDRepository<Currency, String> implements CurrencyRepository, Serializable {
    @Override
    public Currency findByCode(String code) {
        TypedQuery<Currency> query = em.createNamedQuery("Currency.findByCode", Currency.class)
                .setParameter("code", code)
                .setMaxResults(1);
        return Util.getSingle(query);
    }
}
