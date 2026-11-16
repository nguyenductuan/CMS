package com.vt.cms.product.domain.repository;

import com.vt.cms.product.domain.entity.ProductReview;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductReviewRepository {
  int isEligibleOrderItem(
      @Param("orderId") Integer orderId,
      @Param("productId") Integer productId,
      @Param("skuId") Integer skuId,
      @Param("username") String username);

  int insertReview(ProductReview review);

  int countByOrderAndSku(@Param("orderId") Integer orderId, @Param("skuId") Integer skuId);

  List<ProductReview> findByProductId(@Param("productId") Integer productId);

  BigDecimal averageRating(@Param("productId") Integer productId);

  // ------------------------------//
  int upsertProductStats(
      @Param("productId") Integer productId,
      @Param("averageRating") BigDecimal averageRating,
      @Param("totalReviews") Integer totalReviews);

  int countReviewsByProduct(Integer productId);
  // ----------------------------//
}
