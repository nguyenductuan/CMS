package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private int productId;
    private Integer quantity;
    private BigDecimal price;
  private  BigDecimal totalprice;
    private Integer orderId;
    private Integer skucode;

}