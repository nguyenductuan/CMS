package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {
    private String id;
    private Integer cartId;
    private int productId;
    private Integer skuId;
    private Integer quantity;
    private String productName;
    private BigDecimal productPrice;
}
