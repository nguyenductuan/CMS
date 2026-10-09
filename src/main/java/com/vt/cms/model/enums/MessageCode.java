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
    PRICE_NOT_FOUND_SKU(
            "PRICE_NOT_FOUND_SKU",
            "Giá sản phẩm không tồn tại"
    ),
    STOCK_NOT_ENOUGH(
            "STOCK_NOT_ENOUGH",
            "Số lượng sản phẩm trong kho không đủ"
    ),
    INVALID_QUANTITY(
            "INVALID_QUANTITY",
            "Số lượng sản phẩm không hợp lệ"
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

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
