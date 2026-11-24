package com.vt.cms.order.application.dto.request;

import java.math.BigDecimal;
import lombok.Data;

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
