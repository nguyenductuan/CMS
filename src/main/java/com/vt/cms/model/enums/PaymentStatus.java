package com.vt.cms.model.enums;

public enum PaymentStatus {

    PAYMENT_SUCCESS(
            "PAYMENT_SUCCESS",
            "Thành công"
    ),
    PAYMENT_FAILED(
            "PAYMENT_FAILED",
            "Thanh toán thất bại"
    );



    private final String code;
    private final String message;

    PaymentStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}




