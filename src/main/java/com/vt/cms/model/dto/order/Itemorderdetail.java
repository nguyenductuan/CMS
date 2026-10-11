package com.vt.cms.model.dto.order;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Itemorderdetail {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer campaignId;
    private Integer stockCampaign;
    private String skuCode;
    private Integer skuId;
    private String jsonImages;
    private String priceCampaign;
    private BigDecimal priceDiscountCampaign;
    private int quantity;

}
