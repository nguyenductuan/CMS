package com.vt.cms.order.domain.entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItem {
  private Integer id;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal originalPrice;
  //  private Integer product_id;
  private BigDecimal total_price;
  private Integer order_id;
  private Integer sku_id;
  //  private Integer campaign_id;
  private Integer totalWeight;
}
