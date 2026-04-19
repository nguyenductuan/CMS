package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}
