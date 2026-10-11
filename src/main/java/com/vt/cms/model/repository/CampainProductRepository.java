package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CampainProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainProductRepository {
    void savecampainproduct(CampainProduct campainProduct);
    int deductStock( @Param("campainId") Integer campainId,
                     @Param("SkuId") Integer skuId,
                     @Param("quantity") Integer quantity);
}
