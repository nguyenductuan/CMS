package com.vt.cms.service;

import com.vt.cms.model.resp.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse detail(Integer id);

    List<ProductResponse> listproduct();
}
