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

    /**
     * Phương thức get code cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getCode() {
        return code;
    }

    /**
     * Phương thức get description cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getDescription() {
        return description;
    }
}
