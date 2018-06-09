package com.hengyi.japp.fund.domain;

import javax.persistence.*;

/**
 * Created by jzb on 16-10-20.
 */
@Entity
@Table(name = "T_FUND")
@NamedQueries({
        @NamedQuery(name = "Fund.queryByCorpCurDate", query = "SELECT o FROM Fund o WHERE o.corporation IN :corporations AND o.currency IN :currencies AND o.date BETWEEN :sDate AND :eDate AND o.deleted=FALSE"),
})
public class Fund extends AbstractFundlikeEntity {
    @ManyToOne
    private DayFundPlan dayFundPlan;

    public DayFundPlan getDayFundPlan() {
        return dayFundPlan;
    }

    public void setDayFundPlan(DayFundPlan dayFundPlan) {
        this.dayFundPlan = dayFundPlan;
    }

}
