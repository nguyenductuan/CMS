package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDetailResponse {
    private String id;
    private String campaignId;
    private String name;
    private BigDecimal priceCampaign;
    private BigDecimal priceDiscountCampaign;
    private String description;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime approvedTime;
    private Integer campaignStatus;
    private BigDecimal salesCommission;
    private BigDecimal productCommission;
    private Integer minQuantity;
    private Integer maxQuantity;
    private List<String> categoryIds;
    private List<SkuResponse> skus;
    private Integer totalSold;
    private BigDecimal ratingAvg;
    private LocalDateTime campaignStartAt;
    private LocalDateTime campaignEndAt;
}
