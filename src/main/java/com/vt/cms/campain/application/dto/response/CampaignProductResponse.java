package com.vt.cms.campain.application.dto.response;

import com.vt.cms.campain.domain.enums.CampainStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignProductResponse {
  private String id;
  private Integer campaignId;
  //  private String campaignId;
  private Integer productId;
  private String productName;
  private Integer skuId;

  // Lấy từ product_sku
  private String attribute;
  private String typeProduct;

  // Lấy từ campaign_product
  private BigDecimal price;
  private BigDecimal disCountPrice;
  private Integer stockCampaign;
  private String imageUrlSku;
  private CampainStatus status;
  private BigDecimal discount;
}
