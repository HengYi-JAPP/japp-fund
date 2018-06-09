package com.hengyi.japp.fund.domain.log;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_SMSSEND1818LOG")
//@NamedQueries({
//        @NamedQuery(name = "SmsSend1818Log.findByCode", query = "SELECT o FROM SmsSend1818Log o WHERE o.code=:code"),
//})
public class SmsSend1818Log extends AbstractLog {
}
