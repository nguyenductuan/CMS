package com.vt.cms.order.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vt.cms.campain.domain.enums.CampainStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSkuDTO {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;

  private String productName;
  private String skuCode;
  private String attribute;
  private String typeProduct;
  private BigDecimal price;
  private Integer stock;
  private String weightGram;
  private String lengthCm;
  private Double widthCm;
  private String heightCm;
  private String imageSkuUrl;
  private String imageProductUrl;
  private CampainStatus status;
}
