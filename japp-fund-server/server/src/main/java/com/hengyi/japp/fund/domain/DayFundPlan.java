package com.hengyi.japp.fund.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_DAYFUNDPLAN")
@NamedQueries({
        @NamedQuery(name = "DayFundPlan.queryByCorpCurDate", query = "SELECT o FROM DayFundPlan o WHERE o.corporation IN :corporations AND o.currency IN :currencies AND o.date BETWEEN :sDate AND :eDate AND o.deleted=FALSE"),
})
public class DayFundPlan extends AbstractFundlikeEntity {
}
