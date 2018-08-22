package com.hengyi.japp.fund.exception;

import com.github.ixtf.japp.core.exception.JException;

import static com.github.ixtf.japp.core.Constant.ErrorCode.SYSTEM;

/**
 * Created by jzb on 16-11-20.
 */
public class PermissionInputException extends JException {
    public PermissionInputException() {
        super(SYSTEM);
    }
}
