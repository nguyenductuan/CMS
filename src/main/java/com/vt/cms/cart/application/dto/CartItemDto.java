package com.vt.cms.cart.application.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}
