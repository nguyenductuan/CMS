package com.vt.cms.order.application.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SkuPreviewDto {
  private Integer campaign_id;
  private Integer product_id;
  private Integer skuId;
  private String product_name;
  private BigDecimal price;
  private BigDecimal priceDiscount;
  private Integer quantity;
  private String skuImageUrl;
  private String typeProduct;
  private String attribute;
  private Integer stockCampaign;
}
