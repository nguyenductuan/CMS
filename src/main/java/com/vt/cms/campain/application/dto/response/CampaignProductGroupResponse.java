package com.vt.cms.campain.application.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class CampaignProductGroupResponse {
  private Integer productId;
  private String productName;
  private List<CampaignProductResponse> campaignProduct;
}
