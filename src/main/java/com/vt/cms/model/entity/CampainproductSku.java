package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CampainproductSku {
    private int productID;
    private BigDecimal discountPrice;
    private String image;
    private String status;
}
