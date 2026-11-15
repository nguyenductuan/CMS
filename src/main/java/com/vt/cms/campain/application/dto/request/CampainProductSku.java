package com.vt.cms.campain.application.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CampainProductSku {
  private Integer skuId;
  private BigDecimal discountPrice;
  private BigDecimal price;
  private String image;
  private Integer stock_campaign;
  private String status;
}
