package com.vt.cms.campain.domain.repository;

import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.domain.enity.CampainProduct;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainProductRepository {
  List<CampaignProductResponse> findCampainProductById(@Param("campaign_id") Integer campaignId);

  List<CampainProduct> findCampaignProductByCampaignIdIn(Collection<Integer> ids);

  void savecampainproduct(CampainProduct campainProduct);

  boolean existsAssociationByProductId(@Param("productId") Integer productId);

  int decreaseStock(Integer campaignProductId, Integer quantity);

  Collection<CampainProduct> findAllByCampaignIds(@Param("campaignIds") List<Integer> campaignIds);
}
