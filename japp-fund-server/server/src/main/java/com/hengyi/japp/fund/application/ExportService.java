package com.hengyi.japp.fund.application;

import com.hengyi.japp.fund.domain.Corporation;
import com.hengyi.japp.fund.domain.Currency;

import javax.ws.rs.core.StreamingOutput;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;

/**
 * Created by jzb on 16-10-28.
 */
public interface ExportService {

    StreamingOutput export(Principal principal, Set<Corporation> corporations, Set<Currency> currencies, int year, int month, final LocalDate ldDivide) throws Exception;

    StreamingOutput exportSum(Principal principal, Set<Corporation> corporations, Set<Currency> currencies, int year, int month, final LocalDate ldDivide) throws Exception;
}
