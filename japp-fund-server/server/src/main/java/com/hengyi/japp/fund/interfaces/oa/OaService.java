package com.hengyi.japp.fund.interfaces.oa;

import com.hengyi.japp.fund.application.command.OperatorImportCommand;

import java.util.Collection;

/**
 * Created by jzb on 16-12-3.
 */
public interface OaService {

    Collection<OperatorSuggest> suggestOperators(String q) throws Exception;

    OperatorSuggest findOperatorSuggest(OperatorImportCommand command) throws Exception;
}
