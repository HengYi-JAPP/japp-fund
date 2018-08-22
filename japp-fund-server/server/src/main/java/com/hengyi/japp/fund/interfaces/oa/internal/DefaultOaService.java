package com.hengyi.japp.fund.interfaces.oa.internal;

import com.github.ixtf.japp.core.J;
import com.hengyi.japp.fund.Constant;
import com.hengyi.japp.fund.application.command.OperatorImportCommand;
import com.hengyi.japp.fund.domain.repository.OperatorRepository;
import com.hengyi.japp.fund.interfaces.oa.OaService;
import com.hengyi.japp.fund.interfaces.oa.OperatorSuggest;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jzb on 16-12-3.
 */
@Stateless
public class DefaultOaService implements OaService {
    private static final Function<Map<String, Object>, OperatorSuggest> operatorSuggestF = it -> {
        OperatorSuggest res = new OperatorSuggest();
        res.setOaId((String) it.get("oaId"));
        res.setHrId((String) it.get("hrId"));
        res.setName((String) it.get("name"));
        return res;
    };
    @Resource(lookup = Constant.OA_DS)
    private DataSource oaDS;
    @Inject
    private OperatorRepository operatorRepository;

    @Override
    public Collection<OperatorSuggest> suggestOperators(String q) throws Exception {
        Predicate<OperatorSuggest> predicate1 = suggest -> operatorRepository.findByHrIdOrOaId(suggest.getOaId()) == null;
        Predicate<OperatorSuggest> predicate2 = suggest -> operatorRepository.findByHrIdOrOaId(suggest.getHrId()) == null;
        String sql = "SELECT loginid as oaId,lastname as name,workcode as hrId FROM HrmResource WHERE loginid<>'''' AND lastname LIKE ''{0}%'' OR loginid LIKE ''{0}%'' OR workcode LIKE ''%{0}''";
        sql = MessageFormat.format(sql, q);
        return new QueryRunner(oaDS).query(sql, new MapListHandler()).stream()
                .map(operatorSuggestF)
                .filter(predicate1.and(predicate2))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public OperatorSuggest findOperatorSuggest(OperatorImportCommand command) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT loginid as oaId,lastname as name,workcode as hrId FROM HrmResource WHERE loginid<>''");
        final String hrId = command.getHrId();
        if (J.nonBlank(hrId)) {
            sb.append(" AND workcode='").append(hrId).append("'");
        }
        final String oaId = command.getOaId();
        if (J.nonBlank(oaId)) {
            sb.append(" AND loginid='").append(oaId).append("'");
        }
        return new QueryRunner(oaDS).query(sb.toString(), new MapListHandler()).stream()
                .map(operatorSuggestF)
//                .filter(it -> Objects.equals(hrId, it.getHrId()) && Objects.equals(oaId, it.getOaId()))
                .findFirst()
                .get();
    }
}
