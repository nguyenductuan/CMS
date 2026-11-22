package com.vt.cms.campain.application.service.mapper;

import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enity.CampainProduct;
import com.vt.cms.campain.domain.enums.CampainStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CampaignEntityMapper {
  public static CampainResponse toCampainInfo(Campain campain) {
    CampainResponse campainResponse = new CampainResponse();
    campainResponse.setStartTime(campain.getStartTime());
    campainResponse.setEndTime(campain.getEndTime());
    campainResponse.setId(campain.getCampaignId());
    campainResponse.setName(campain.getName());
    campainResponse.setStatus(campain.getStatus());
    campainResponse.setDiscount(campain.getDiscount());
    return campainResponse;
  }

  public static CampaignProductResponse toCampainProductInfo(CampainProduct campainProduct) {
    CampaignProductResponse campaignProductResponse = new CampaignProductResponse();
    campaignProductResponse.setCampaignId(campainProduct.getCampaignId());
    campaignProductResponse.setProductId(campainProduct.getProductId());
    campaignProductResponse.setSkuId(campainProduct.getSkuId());
    campaignProductResponse.setStockCampaign(campainProduct.getStockCampaign());
    campaignProductResponse.setDisCountPrice(campainProduct.getPrice_discount());
    return campaignProductResponse;
  }



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
