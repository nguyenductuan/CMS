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
  /**
   * Thực hiện truy cập dữ liệu detail product. Các tham số và giá trị trả về được mô tả bởi chữ ký
   * phương thức.
   */
  ProductResponse detailProduct(@Param("productId") Integer productId);

  /** Lấy danh sách listproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  List<ProductResponse> listproduct(Pagerequest request);

  /** Đếm số lượng countproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  long countproduct(Pagerequest request);

  /**
   * Thêm bản ghi insertproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
  int insertproduct(Product product);

  /** Cập nhật update product. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức. */
  int updateProduct(@Param("product") Product product, @Param("id") Integer id);

  /**
   * Lấy dữ liệu get product by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
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
}
