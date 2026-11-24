package com.vt.cms.order.application.dto.request;

import lombok.Data;

@Data
public class ItemRequest {
  private Integer productId;
  private Integer campainId;
  private int quantity;
  private Integer SkuId;
}
