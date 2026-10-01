package com.vt.cms.Exception;

import com.vt.cms.model.enums.MessageCode;

public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.code = messageCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
