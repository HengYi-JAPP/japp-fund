package com.hengyi.japp.fund.exception;

import org.jzb.exception.JException;

import java.util.Map;

/**
 * Created by jzb on 16-11-20.
 */
public class PermissionInputException extends JException {
    public PermissionInputException(String errorCode, Map<String, String> params) {
        super(errorCode, params);
    }
}
