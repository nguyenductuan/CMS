package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product_Sku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSkuRepository {
    int inserproductsku(Product_Sku productSku);
}
