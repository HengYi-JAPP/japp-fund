package com.hengyi.japp.fund.domain.repository;


import com.hengyi.japp.fund.domain.log.SmsSend1818Log;

/**
 * Created by jzb on 17-5-10.
 */
public interface SmsSend1818LogRepository {

    SmsSend1818Log save(SmsSend1818Log log);

    SmsSend1818Log find(String id);
}
