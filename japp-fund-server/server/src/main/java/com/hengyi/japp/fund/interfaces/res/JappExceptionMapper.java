package com.hengyi.japp.fund.interfaces.res;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ixtf.japp.core.exception.JException;
import com.github.ixtf.japp.core.exception.JMultiException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.github.ixtf.japp.core.Constant.ErrorCode.SYSTEM;
import static com.github.ixtf.japp.core.Constant.ErrorCode.TOKEN;
import static com.github.ixtf.japp.core.Constant.MAPPER;

/**
 * Created by jzb on 16-10-26.
 */
@Provider
public class JappExceptionMapper implements ExceptionMapper<Throwable> {
    @Inject
    private Logger log;

    @Override
    public Response toResponse(Throwable ex) {
        Response.ResponseBuilder builder = Response.status(Status.FORBIDDEN);
        ArrayNode errors = MAPPER.createArrayNode();
        if (ex instanceof JException) {
            JException jex = (JException) ex;
            errors.add(jex.toJsonNode());
        } else if (ex instanceof JMultiException) {
            JMultiException jex = (JMultiException) ex;
            jex.getExceptions().stream()
                    .map(JException::toJsonNode)
                    .forEach(errors::add);
        } else if (ex instanceof ExpiredJwtException) {
            errors.add(MAPPER.createObjectNode().put("errorCode", TOKEN));
        } else {
            log.error("", ex);
            ObjectNode error = MAPPER.createObjectNode()
                    .put("errorCode", SYSTEM)
                    .put("errorMsg", ex.getLocalizedMessage());
            errors.add(error);
        }
        return builder.entity(errors).build();
    }
}
