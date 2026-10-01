package com.vt.cms.model.entity;

import com.vt.cms.model.resp.SkuResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<SkuResponse> skus;


    // =========================
    // Statistics
    // =========================

    private Integer totalSold;

    private BigDecimal ratingAvg;
    }