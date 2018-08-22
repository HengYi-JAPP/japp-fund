package com.hengyi.japp.fund.client.interfaces.res;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import static com.github.ixtf.japp.core.Constant.MAPPER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Provider
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return MAPPER;
    }

}
