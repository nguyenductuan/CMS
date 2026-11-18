package com.vt.cms.campain.domain.repository;

import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.domain.enity.CampainProduct;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainProductRepository {
  List<CampaignProductResponse> findCampainProductById(@Param("campain_id") Integer campain_id);

  void savecampainproduct(CampainProduct campainProduct);

  boolean existsAssociationByProductId(@Param("productId") Integer productId);

  int deductStock(
      @Param("campainId") Integer campainId,
      @Param("SkuId") Integer skuId,
      @Param("quantity") Integer quantity);
}
