package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product_Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductSkuRepository {
    int inserproductsku(Product_Sku productSku);
    List<Product_Sku> findByProductId(Integer product_id);
    Product_Sku findActiveById(@Param("skuId") Integer skuId);
    int softDeleteByProductId(@Param("productId") Integer productId);
    int updateByProductIdAndSkuCode(@Param("sku") Product_Sku sku);
}
