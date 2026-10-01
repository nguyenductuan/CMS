package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDetailResponse {
    private Integer id;
    private Integer campaignId;
    private String name;
    private BigDecimal priceCampaign;
    private BigDecimal priceDiscountCampaign;
    private String description;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime approvedTime;
    private String campaignStatus;
    private String attributesName;
    private String typeProductsName;
    private List<SkuResponse> skus;
    private Integer totalSold;
    private BigDecimal ratingAvg;
    private LocalDateTime campaignStartAt;
    private LocalDateTime campaignEndAt;
}
