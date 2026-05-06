package com.vt.cms.model.entity;

import lombok.Data;

@Data
public class OrderItem {
    private int productId;
    private Integer quantity;
    private int price;
    private int orderId;
}