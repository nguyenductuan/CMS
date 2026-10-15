package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Integer productId;
    private Integer quantity;
    private String productName;
    private BigDecimal priceCampain;
    private BigDecimal priceDiscount;
    private  Integer stockCampain;
    private  Integer campaignId;
}
