package com.vt.cms.order.infrastructure.adapter;

import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.CampainService;
import com.vt.cms.order.application.service.port.CampaignPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class OrderCampaignAdapter implements CampaignPort {

  private final CampainService campainService;

  public OrderCampaignAdapter(CampainService campainService) {
    this.campainService = campainService;
  }

  @Override
  public List<CampainResponse> getCampaignInfoById(Set<Integer> campainids) {
    return campainService.getCampaignInfoById(campainids);
  }

  @Override
  public List<CampaignProductResponse> findCampaignProductByCampaignIdIn(Set<Integer> campainids) {
    return campainService.findCampaignProductByCampaignIdIn(campainids);
  }

  @Override
  public boolean isCampaignExpried(LocalDateTime endtime) {
    return campainService.isCampaignExpried(endtime);
  }

  @Override
  public int decreaseStock(Integer campaignProductId, Integer quantity) {
    return campainService.decreaseStock(campaignProductId, quantity);
  }

  @Override
  public int decreaseTotalStock(Integer campaignId, Integer quantity) {
    return campainService.decreaseTotalStock(campaignId, quantity);
  }
}
