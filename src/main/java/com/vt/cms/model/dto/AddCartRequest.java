package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class AddCartRequest {
    private Integer userId;
    private int productId;
    private int quantity;
}
