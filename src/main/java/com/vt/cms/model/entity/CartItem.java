package com.vt.cms.model.entity;

import lombok.Data;

@Data
public class CartItem {
    private String id;
    private Integer cartId;
    private int productId;
    private Integer quantity;
    private String productName;
    private int productPrice;
//    private Cart cart;
}
