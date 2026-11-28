package com.vt.cms.order.application.dto.response;

import lombok.Data;

@Data
public class OrderItemDto {
  private Integer orderId;
  private Integer skuId;
  private String skuCode;
  private Integer quantity;
  private String attribute;
  private String status;
  private String typeProducts;
  private String heightCm;
  private String lengthCm;
  private String weightGram;
  private String weightCm;
}
