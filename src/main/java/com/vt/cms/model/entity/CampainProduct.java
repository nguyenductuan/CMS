package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CampainProduct {
    private Long id;

    private LocalDateTime createdAt;
    private String createdBy;
    private String isDeleted;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer campaignId;
    private Integer productId;
    private Integer skuId;
    private Integer stockCampaign;
    private BigDecimal price;
    private BigDecimal price_discount;
    private String discountAmount;
    private String status;


}
