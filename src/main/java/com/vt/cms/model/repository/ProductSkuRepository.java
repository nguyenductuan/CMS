package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product_Sku;
import org.mapstruct.Mapper;

@Mapper
public interface ProductSkuRepository {
    void saveproduct(Product_Sku productSku);
}
