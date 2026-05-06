package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product;
import com.vt.cms.model.resp.ProductResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {
    Product detailProduct(Integer id);

    List<ProductResponse> listproduct();
}
