package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductRepository {
    Product detailProduct(Integer id);
}
