package com.hengyi.japp.fund.domain.event;

/**
 * Created by jzb on 16-10-28.
 */
public interface EventRepository<T extends EventEntity> {
    T save(T event) throws Exception;
}
