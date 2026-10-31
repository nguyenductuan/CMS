package com.vt.cms.model.repository;

import com.vt.cms.model.entity.ProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProductReviewRepository {
    int isEligibleOrderItem(@Param("orderId") Integer orderId,
                            @Param("productId") Integer productId,
                            @Param("skuId") Integer skuId,
                            @Param("username") String username);
    int insertReview(ProductReview review);
    int countByOrderAndSku(@Param("orderId") Integer orderId, @Param("skuId") Integer skuId);
    List<ProductReview> findByProductId(@Param("productId") Integer productId);
    BigDecimal averageRating(@Param("productId") Integer productId);
}
