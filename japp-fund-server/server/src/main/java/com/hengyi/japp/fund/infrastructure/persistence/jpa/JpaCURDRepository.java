package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.share.CURDEntity;
import com.hengyi.japp.fund.share.CURDEntityRepository;
import org.jzb.J;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-10-28.
 */
public abstract class JpaCURDRepository<E extends CURDEntity<ID>, ID> implements CURDEntityRepository<E, ID>, Serializable {
    @PersistenceContext
    protected EntityManager em;
    private Class<E> entiyClass;

    @PostConstruct
    void entiyClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entiyClass = (Class<E>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public E save(E entity) {
        return em.merge(entity);
    }

    @Override
    public E find(ID id) {
        return em.find(entiyClass, id);
    }

    @Override
    public void delete(ID id) {
        E e = find(id);
        e.setDeleted(true);
        em.merge(e);
    }

    @Override
    public Stream<E> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> resultCq = cb.createQuery(entiyClass).distinct(true);
        Root<E> root = resultCq.from(entiyClass);
        resultCq.select(root);
        return em.createQuery(resultCq)
                .getResultList()
                .stream()
                .filter(it -> !it.isDeleted());
    }

    @Override
    public Stream<E> queryByIds(Collection<? extends ID> ids) {
        return J.emptyIfNull(ids)
                .stream()
                .map(this::find);
    }
}
