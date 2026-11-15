package com.vt.cms.product.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProductDetail {

  // =========================
  // Product
  // =========================

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

  // =========================
  // Campaign
  // =========================

  private String campaignStatus;

  private String campaignName;
  private String campaignDescription;
  private BigDecimal campaignDiscount;

  private LocalDateTime campaignStartAt;

  private LocalDateTime campaignEndAt;

  // =========================
  // Product attributes
  // =========================

  private String attributesName;

  private String typeProductsName;

  // =========================
  // SKU
  // =========================

  //    private List<SkuResponse> skus;
  private String skus;

  // =========================
  // Statistics
  // =========================

  private Integer totalSold;

  private BigDecimal ratingAvg;
}
