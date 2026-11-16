package com.vt.cms.campain.application.service.mapper;

import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enums.CampainStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CampaignEntityMapper {
  /** Thực hiện to new campaign. Tham số và kết quả tuân theo chữ ký phương thức. */
  public Campain toNewCampaign(CreatedCampainRequest request, Integer userId) {
    Campain campaign = new Campain();
    campaign.setName(request.getName());
    campaign.setDescription(request.getDescription());
    campaign.setStartTime(request.getStartTime());
    campaign.setEndTime(request.getEndTime());
    campaign.setDiscount(request.getDiscount());
    campaign.setStatus(CampainStatus.PENDING);
    campaign.setCreatedAt(LocalDateTime.now());
    campaign.setCreatedBy(userId.toString());
    campaign.setIsDeleted("false");
    return campaign;
  }
}
