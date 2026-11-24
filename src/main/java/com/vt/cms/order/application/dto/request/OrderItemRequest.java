package com.vt.cms.order.application.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
  private Integer SkuId;
  private Integer quantity;
}
