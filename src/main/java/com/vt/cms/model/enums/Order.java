package com.vt.cms.model.enums;

public enum Order {
    ORDER_SUCCESS(
            "ORDER_SUCCESS",
            "Thành công"
    ),
    ORDER_CANCEL(
            "ORDER_CANCEL",
            "Hủy đơn hàng thành công"
    );


    private final String code;
    private final String message;

    Order (String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Phương thức get code cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getCode() {
        return code;
    }

    /**
     * Phương thức get message cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getMessage() {
        return message;
    }
}
