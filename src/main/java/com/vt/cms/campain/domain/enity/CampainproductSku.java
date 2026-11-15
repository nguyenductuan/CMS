package com.vt.cms.campain.domain.enity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CampainproductSku {
  private int productID;
  private BigDecimal discountPrice;
  private String image;
  private String status;
}
