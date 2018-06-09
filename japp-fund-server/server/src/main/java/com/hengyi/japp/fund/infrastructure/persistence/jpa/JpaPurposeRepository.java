package com.hengyi.japp.fund.infrastructure.persistence.jpa;

import com.hengyi.japp.fund.domain.Purpose;
import com.hengyi.japp.fund.domain.repository.PurposeRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

/**
 * Created by jzb on 16-10-20.
 */
@ApplicationScoped
public class JpaPurposeRepository extends JpaCURDRepository<Purpose, String> implements PurposeRepository, Serializable {
}
