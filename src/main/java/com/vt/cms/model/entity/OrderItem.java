package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Integer quantity;
    private BigDecimal price;
    private Integer product_id;
    private  BigDecimal total_price;
    private Integer order_id;
    private Integer sku_id;

}