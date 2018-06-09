package com.hengyi.japp.fund.interfaces.res.resources;

import com.hengyi.japp.fund.application.FundlikeService;
import com.hengyi.japp.fund.application.command.BatchFundlikeUpdateCommand;
import com.hengyi.japp.fund.domain.AbstractFundlikeEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 16-10-26.
 */
@Stateless
@Path("batchFundlikes")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BatchFundlikeResource {
    @Inject
    private FundlikeService fundlikeService;

    @POST
    public Collection<? extends AbstractFundlikeEntity> create(@Context SecurityContext sc,
                                                               @Valid @NotBlank @QueryParam("monthFundPlanId") String monthFundPlanId,
                                                               @Valid @NotNull BatchFundlikeUpdateCommand command) throws Exception {
        return fundlikeService.create(sc.getUserPrincipal(), monthFundPlanId, command);
    }

}
