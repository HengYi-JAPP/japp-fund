package com.hengyi.japp.fund.share;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by jzb on 16-11-19.
 */
public interface CURDEntityRepository<E extends CURDEntity<ID>, ID> {
    E save(E t);

    E find(ID id);

    void delete(ID id);

    Stream<E> findAll();

    Stream<E> queryByIds(Collection<? extends ID> ids);
}
