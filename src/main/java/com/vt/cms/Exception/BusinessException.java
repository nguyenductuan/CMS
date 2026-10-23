package com.vt.cms.Exception;

import com.vt.cms.model.enums.MessageCode;

public class BusinessException extends RuntimeException {

    private final String code;

    /**
     * Thực hiện xử lý business exception. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public BusinessException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.code = messageCode.getCode();
    }

    /**
     * Lấy dữ liệu get code. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public String getCode() {
        return code;
    }
}
