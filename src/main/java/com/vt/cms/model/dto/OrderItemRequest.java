package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Integer productId;
    private Integer quantity;
    private String ShippingMethodId;
    private String ShippingMethodName;
    // gửi thng tin địa chỉ kho của seller tạo chến dịch chứa sp đó


}
