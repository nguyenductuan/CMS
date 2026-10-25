package com.vt.cms.model.enums;

public enum MessageCode {
    SHIPPING_NOT_FOUND(
            "SHIPPING_NOT_FOUND",
            "Phương thức vận chuyển không tồn tại"
    ),
    INVALID_SHIPPING_FEE(
            "INVALID_SHIPPING_FEE",
            "Phí vận chuyển không hợp lệ"
    ),
    INVALID_ORDER(
            "INVALID_ORDER",
            "Thông tin đơn hàng không hợp lệ"
    ),
    INVALID_PRICE(
            "INVALID_PRICE",
            "Giá sản phẩm không hợp lệ"
    ),
    PRODUCT_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            "Sản phẩm không tồn tại"
    ),
    SKU_NOT_FOUND(
            "SKU_NOT_FOUND",
            "SKU không tồn tại"
    ),
    SKU_NOT_ACTIVE(
            "SKU_NOT_ACTIVE",
            "SKU không ở trạng thái ACTIVE"
    ),
    PRICE_NOT_FOUND_SKU(
            "PRICE_NOT_FOUND_SKU",
            "Giá sản phẩm không tồn tại"
    ),
    STOCK_NOT_ENOUGH(
            "STOCK_NOT_ENOUGH",
            "Số lượng sản phẩm trong kho không đủ"
    ),
    STOCK_INVALID(
            "STOCK_INVALID","Số lượng tồn kho không hợp lệ"
    ),
    STOCK_INSUFFICIENT(
            " STOCK_INSUFFICIENT", "Số lượng tồn kho không đủ"
    ),
    INVALID_QUANTITY(
            "INVALID_QUANTITY",
            "Số lượng sản phẩm không hợp lệ"
    ),
    CAMPAIN_STATUS_INVALID (
            "CAMPAIN_STATUS_INVALID",
            "Trạng thái campain không hợp lệ"
    ),
    PRODUCT_SKU_NOT_FOUND(
            "PRODUCT_SKU_NOT_FOUND",
            "SKU sản phẩm không tồn tại"
    );

    private final String code;
    private final String message;

    MessageCode(String code, String message) {
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
