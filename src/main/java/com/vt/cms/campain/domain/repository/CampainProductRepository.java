package com.vt.cms.campain.domain.repository;

import com.vt.cms.campain.domain.enity.CampainProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainProductRepository {
  void savecampainproduct(CampainProduct campainProduct);

  boolean existsAssociationByProductId(@Param("productId") Integer productId);

  int deductStock(
      @Param("campainId") Integer campainId,
      @Param("SkuId") Integer skuId,
      @Param("quantity") Integer quantity);
}
