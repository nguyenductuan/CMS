package com.vt.cms.cart.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Cart {
    List<CartItem> items;
    private Integer id;
    private Integer userId;
}
