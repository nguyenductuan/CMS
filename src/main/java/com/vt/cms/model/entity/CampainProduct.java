package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CampainProduct {
private int campainid;
private long productId;
private LocalDateTime createddate;
    private LocalDateTime updateddate;
    private String createdAt;
    private String createdBy;
    private String isDeleted;
    private String updatedAt;
    private String updatedBy;
    private Integer skuId;
    private String stockCampaign;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String discountAmount;
    private String status;

}
