package com.vt.cms.model.enums;

public enum OrderStatus {
    WAIT_PAYMENT("WAIT_PAYMENT", "Chờ thanh toán"),
    PAID("CONFIRMED", "Đã thanh toán"),
    WAITING_FOR_DELIVERY("WAITING_FOR_DELIVERY", "Chờ chuẩn bị lấy hàng"),
    PREPARING("PREPARING", "Đã chuẩn bị hàng"),
    SHIPPING("SHIPPING", "Đang giao hàng"),
    DELIVERED("DELIVERED", "Shipper giao thành công"),
    RECEIVED("RECEIVED", "Khách hàng đã nhận hàng"),
    COMPLETED("COMPLETED", "Đã hoàn thành"),
    CANCELLED("CANCELLED", "Đã hủy"),
    REFUND_PENDING("REFUND_PENDING", "Chờ hoàn tiền"),
    RETURN_REQUESTED("RETURN_REQUESTED", "Đang yêu cầu trả hàng"),
    PENDING_SELLER_REVIEW("PENDING_SELLER_REVIEW", "Chờ Seller xét duyệt"),
    PENDING_ADMIN_REVIEW("PENDING_ADMIN_REVIEW", "Chờ Admin xét duyệt"),
    RETURN_APPROVED("RETURN_APPROVED", "Đã duyệt trả hàng"),
    RETURN_REJECTED("RETURN_REJECTED", "Từ chối trả hàng"),
    FAILED("FAILED", "Thất bại");


    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
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


