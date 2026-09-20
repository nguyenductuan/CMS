package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CampainProductSku {
    private Integer skuId;
    private BigDecimal discountPrice;
    private  BigDecimal price;
    private String image;
    private Integer stock;
    private String status;
}
