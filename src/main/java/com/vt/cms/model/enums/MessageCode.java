package com.vt.cms.model.enums;

public enum MessageCode {
    SHIPPING_NOT_FOUND(
            "SHIPPING_NOT_FOUND",
            "Phương thức vận chuyển không tồn tại"
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
