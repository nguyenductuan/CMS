package com.vt.cms.model.repository;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.resp.ProductResponse;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductRepository {
    ProductResponse detailProduct(Integer id);

    List<ProductResponse> listproduct(OrdersRequest request);

    int countproduct();

    int insertproduct(Product product);

    void upload(Product product, Integer id);

    void editstatusproduct(Integer id, String status, LocalDateTime updatedAt);

    void deleteproduct(Integer id);
}
