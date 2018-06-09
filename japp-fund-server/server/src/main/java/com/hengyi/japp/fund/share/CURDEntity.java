package com.hengyi.japp.fund.share;

import java.io.Serializable;

/**
 * Created by jzb on 16-11-19.
 */
public interface CURDEntity<ID> extends Serializable {
    ID getId();

    void setId(ID id);

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
