package com.vt.cms.model.repository;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.resp.ProductResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDateTime;
import java.util.List;
@Mapper
public interface ProductRepository {
    Product detailProduct(@Param("productId") Integer productId);

    List<ProductResponse> listproduct(OrdersRequest request);

    int countproduct(OrdersRequest request);

    int insertproduct(Product product);

    int updateProduct(@Param("product") Product product, @Param("id") Integer id);
    Product getProductById(@Param("id") Integer id);

    int editstatusproduct(@Param("id") Integer id,
                          @Param("status") String status,
                          @Param("updated_at") LocalDateTime updated_at,
                          @Param("approve_by") String approve_by,
                          @Param("approved_time") LocalDateTime approved_time);

    int deleteproduct(@Param("productId") Integer productId);

    List<Product> listproduct(Integer productIds);
    Product getproduct(Integer product_id, Integer campaign_id);
    ProductDetail getproduct1(Integer product_id, Integer campaign_id);


}
