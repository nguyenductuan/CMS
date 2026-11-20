package com.vt.cms.product.domain.repository;

import com.vt.cms.product.domain.entity.Product_Sku;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductSkuRepository {
  List<Product_Sku> findSkuByIds(@Param("skuIds") Set<Integer> ids);

  /**
   * Thực hiện truy cập dữ liệu inserproductsku. Các tham số và giá trị trả về được mô tả bởi chữ ký
   * phương thức.
   */
  int inserproductsku(Product_Sku productSku);

  /**
   * Tìm kiếm find by product id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
  List<Product_Sku> findByProductId(Integer product_id);

  /**
   * Tìm kiếm find not deleted by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương
   * thức.
   */
  Product_Sku findNotDeletedById(@Param("skuId") Integer skuId);

  /**
   * Thực hiện truy cập dữ liệu soft delete by product id. Các tham số và giá trị trả về được mô tả
   * bởi chữ ký phương thức.
   */
  int softDeleteByProductId(@Param("productId") Integer productId);

  /**
   * Cập nhật update by product id and sku code. Các tham số và giá trị trả về được mô tả bởi chữ ký
   * phương thức.
   */
  int updateByProductIdAndSkuCode(@Param("sku") Product_Sku sku);

  void saveSkus(@Param("skus") List<Product_Sku> productSkus);
}
