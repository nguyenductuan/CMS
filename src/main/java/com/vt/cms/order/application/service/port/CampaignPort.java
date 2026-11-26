package com.vt.cms.order.application.service.port;

import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CampaignPort {
  List<CampainResponse> getCampaignInfoById(Set<Integer> campainID);

  List<CampaignProductResponse> findCampaignProductByCampaignIdIn(Set<Integer> campainID);

  boolean isCampaignExpried(LocalDateTime endtime);

  int decreaseStock(Integer campaignProductId, Integer quantity);

  int decreaseTotalStock(Integer campaignId, Integer quantity);
}
