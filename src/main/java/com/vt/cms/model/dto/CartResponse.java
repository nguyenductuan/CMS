package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    List<CartItemDto> cartdto;
    private String userId;
    private Double totalprice;
}
