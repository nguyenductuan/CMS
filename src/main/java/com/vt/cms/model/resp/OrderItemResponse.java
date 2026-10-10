package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Integer productId;
    private Integer quantity;
    private String productName;
    private BigDecimal price_campain;
    private BigDecimal price_discount;
}
