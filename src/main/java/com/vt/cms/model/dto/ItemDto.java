package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {
    private Integer productId;
    private String productName;
    private Integer campaignId;
    private BigDecimal price_campain;
    private BigDecimal price_discount;
    private Integer quantity;
   private Integer stock;

}
