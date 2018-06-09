package com.hengyi.japp.fund.domain.repository;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.share.CURDEntityRepository;

/**
 * Created by jzb on 16-10-20.
 */
public interface CorporationRepository extends CURDEntityRepository<Corporation, String> {
    Corporation findByCode(String code);
}
