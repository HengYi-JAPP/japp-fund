package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.EntityDTO;
import com.hengyi.japp.fund.application.dto.PermissionDTO;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jzb on 16-10-20.
 */
@Data
public class OperatorUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    private Boolean admin = false;
    private Collection<EntityDTO> operatorGroups;
    private Collection<PermissionDTO> permissions;

}
