package com.vt.cms.product.application.dto.product;

import com.vt.cms.campain.domain.enums.CampainStatus;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SkuRequest {
  private String attribute;
  private String type_product;
  private String image_url;
  private String sku_code;
  private BigDecimal price;
  private Integer stock;
  private String weight_gram;
  private String length_cm;
  private String width_cm;
  private String height_cm;
  private CampainStatus status;
  private String attrIndex;
  private Integer typeProductIndex;
}
