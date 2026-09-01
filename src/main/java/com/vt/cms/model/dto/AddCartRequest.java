package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class AddCartRequest {
    private Integer userId;
    private Integer productId;
    private Integer quantity;
}
