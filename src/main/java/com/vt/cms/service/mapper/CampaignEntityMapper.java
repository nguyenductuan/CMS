package com.vt.cms.service.mapper;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.enums.CampainStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CampaignEntityMapper {
    public Campain toNewCampaign(CreatedCampainRequest request) {
        Campain campaign = new Campain();
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setStartTime(request.getStartTime());
        campaign.setEndTime(request.getEndTime());
        campaign.setDiscount(request.getDiscount());
        campaign.setTotalStock(request.getTotal_stock());
        campaign.setStatus(CampainStatus.PENDING_APPROVAL);
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setCreatedBy("SYSTEM");
        campaign.setIsDeleted("false");
        return campaign;
    }
}
