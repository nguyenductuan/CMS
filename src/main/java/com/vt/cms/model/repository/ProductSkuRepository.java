package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product_Sku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuRepository {
    int inserproductsku(Product_Sku productSku);
    List<Product_Sku> findByProductId(Integer product_id);
}
