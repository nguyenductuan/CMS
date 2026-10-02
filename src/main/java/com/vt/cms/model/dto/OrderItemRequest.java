package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemRequest {
    private Integer productId;
    private Integer campainId;
    private List<SkuOrderRequest> item;
    private String shipping_service_code;
    private String shipping_service_name;
    // gửi thng tin địa chỉ kho của seller tạo chến dịch chứa sp đó


}
