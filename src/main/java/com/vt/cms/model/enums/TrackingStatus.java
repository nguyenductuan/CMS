package com.vt.cms.model.enums;

public enum TrackingStatus {

    WAITING_PAYMENT("S06", "Chờ thanh toán", "KH đã tạo thành công đơn, chờ thanh toán"),
    PAID("S02", "Đã thanh toán", "Đơn hàng đã thanh toán thành công"),
    CONFIRMED("S03", "Đã xác nhận", "Đơn hàng đã được xác nhận"),
    SHIPPING("S04", "Đang giao hàng", "Đơn hàng đang được giao"),
    DELIVERED("S05", "Đã giao hàng", "Đơn hàng đã giao thành công"),
    CANCELLED("S07", "Đã hủy", "Đơn hàng đã bị hủy");

    private  final String code;
    private  final String name;
    private final String description;
    TrackingStatus (String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
