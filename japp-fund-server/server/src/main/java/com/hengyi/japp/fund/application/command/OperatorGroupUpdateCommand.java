package com.hengyi.japp.fund.application.command;

import com.hengyi.japp.fund.application.dto.PermissionDTO;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by jzb on 16-10-29.
 */
@Data
public class OperatorGroupUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1)
    private Collection<PermissionDTO> permissions;

}
