package com.vt.cms.order.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderRequest {
  private List<OrderRequest> orders;
  private String Shipping;
  private BigDecimal totalAmount;
}
