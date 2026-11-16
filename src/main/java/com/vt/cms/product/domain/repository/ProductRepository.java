package com.vt.cms.product.domain.repository;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.product.application.dto.response.ProductResponse;
import com.vt.cms.product.domain.entity.Product;
import com.vt.cms.product.domain.entity.ProductDetail;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductRepository {

  int getotalSold(Integer productId);

  ProductResponse detailProduct(@Param("productId") Integer productId);

  List<ProductResponse> listproduct(Pagerequest request);

  long countproduct(Pagerequest request);

  int insertproduct(Product product);

  int updateProduct(@Param("product") Product product, @Param("id") Integer id);

  Product getProductById(@Param("id") Integer id);

  int approveProduct(@Param("product_id") Integer product_id, @Param("user_id") String user_id);

  int rejectProduct(
      @Param("id") Integer id,
      @Param("cancel_by") String cancel_by,
      @Param("reason") String reason);

  //  /** Xóa deleteproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  int deleteproduct(@Param("productId") Integer productId);

  /** Lấy danh sách listproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  List<Product> listproduct(Integer productIds);

  /** Lấy dữ liệu getproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  Product getproduct(Integer product_id, Integer campaign_id);

  /** Lấy dữ liệu getproduct1. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  ProductDetail getproduct1(Integer product_id, Integer campaign_id);

  Double getRating(Integer productId);
}
