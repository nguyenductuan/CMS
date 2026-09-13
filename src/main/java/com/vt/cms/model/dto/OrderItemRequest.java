package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemRequest {
    private Integer productId;
    private Integer campainId;
    private List<SkuOrderRequest> skus;
    private String ShippingMethodId;
    private String ShippingMethodName;
    // gửi thng tin địa chỉ kho của seller tạo chến dịch chứa sp đó


}
