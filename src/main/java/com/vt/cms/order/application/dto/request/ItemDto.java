package com.vt.cms.order.application.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemDto {
  private Integer productId;
  private String productName;
  private Integer campaignId;
  private BigDecimal price_campain;
  private BigDecimal price_discount;
  private Integer quantity;
  private Integer stock;
  private Integer skuId;
}
