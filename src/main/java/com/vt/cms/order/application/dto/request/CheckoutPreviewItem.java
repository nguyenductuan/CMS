package com.vt.cms.order.application.dto.request;

import lombok.Data;

@Data
public class CheckoutPreviewItem {
  private Integer campaign_id;
  private Integer sku_id;
  private Integer product_id;
  private Integer quantity;
}
