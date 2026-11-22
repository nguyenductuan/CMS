package com.vt.cms.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceedStockSkuDTO {
  private ProductSkuDTO sku;
  private Integer stock;
}
