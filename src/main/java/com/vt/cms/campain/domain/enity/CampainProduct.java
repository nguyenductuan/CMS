package com.vt.cms.campain.domain.enity;

import com.vt.cms.campain.domain.enums.CampainStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampainProduct {
  private Integer id;
  private LocalDateTime createdAt;
  private String createdBy;
  private String isDeleted;
  private LocalDateTime updatedAt;
  private String updatedBy;
  private Integer campaignId;
  private Integer productId;
  private Integer skuId;
  private Integer stockCampaign;
  private BigDecimal price;
  private BigDecimal priceDiscount;
  private String discountAmount;
  private CampainStatus status;
}
