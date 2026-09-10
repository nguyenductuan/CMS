package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CampainProductSku {
    private int SkuId;
    private BigDecimal discountPrice;
    private String image;
    private String status;
}
