package com.vt.cms.service;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductDetailResponse;
import com.vt.cms.model.resp.ProductResponse;

import java.util.List;

public interface ProductService {
    void addproduct(ProductRequest productRequest);

    ProductResponse detail(Integer product_id);

    void editproduct(Integer id, ProductRequest productRequest);

    BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request);

    void editstatusproduct(Integer product_id);

    void deleteproduct(Integer product_id);

    ProductDetailResponse product_detail(Integer campainId, Integer productId);


}
