package com.vt.cms.model.enums;

public enum OrderStatus {
    PENDING("PENDING", "Chờ xác nhận"),
    CONFIRMED("CONFIRMED", "Đã thanh toán"),
    WAITING_FOR_DELIVERY(" WAITING_FOR_DELIVERY", "Chờ chuẩn bị lấy hàng"),
    PREPARING(" PREPARING", "Đã chuẩn bị hàng"),
    SHIPPING("SHIPPING", "Đang giao hàng"),
    DELIVERED("DELIVERED", "Đã giao"),
    CANCELLED("CANCELLED", "Đã hủy"),
    FAILED("FAILED", "Thất bại");


    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
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


