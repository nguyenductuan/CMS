package com.vt.cms.model.resp;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Integer productId;
    private Integer quantity;
    private String productName;
    private Integer price;
    private Integer totalPrice;
}
