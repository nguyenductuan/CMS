package com.vt.cms.model.enums;

public enum OrderShippingStatus {
    WAIT_SHIPPING("WAIT_SHIPPING","Chờ giao hàng"),
    SHIPPING("SHIPPING","Đang giao hàng");


    private final String code;
    private final String description;

    OrderShippingStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
