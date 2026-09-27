package com.vt.cms.model.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SkuResponse {
    private String id;
    private String skuCode;
    private String attribute;
    private String typeProduct;
    private BigDecimal price;
    private Integer stock;
    private Integer weightGram;
    private BigDecimal lengthCm;
    private BigDecimal widthCm;
    private BigDecimal heightCm;
    private String imageUrl;
    private String status;
    private String campaignProductStatus;
    private BigDecimal priceCampaign;
    private BigDecimal priceDiscountCampaign;
    private Integer stockCampaign;
}
