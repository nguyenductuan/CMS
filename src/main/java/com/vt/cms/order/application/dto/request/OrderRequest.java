package com.vt.cms.order.application.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class OrderRequest {
  private List<OrderItemRequest> item;
  private Integer productId;
  private Integer campaignId;
  private String shipping_service_code;
  private String shipping_service_name;
}
