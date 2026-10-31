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
    /**
     * Thực hiện truy cập dữ liệu detail product. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    ProductResponse  detailProduct(@Param("productId") Integer productId);

    /**
     * Lấy danh sách listproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<ProductResponse> listproduct(OrdersRequest request);

    /**
     * Đếm số lượng countproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    long countproduct(OrdersRequest request);

    /**
     * Thêm bản ghi insertproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int insertproduct(Product product);

    /**
     * Cập nhật update product. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int updateProduct(@Param("product") Product product, @Param("id") Integer id);
    /**
     * Lấy dữ liệu get product by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Product getProductById(@Param("id") Integer id);

    /**
     * Chỉnh sửa editstatusproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int editstatusproduct(@Param("id") Integer id,
                          @Param("status") String status,
                          @Param("updated_at") LocalDateTime updated_at,
                          @Param("approve_by") String approve_by,
                          @Param("approved_time") LocalDateTime approved_time,
                          @Param("approve_by_role") String approve_by_role);

    /**
     * Xóa deleteproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int deleteproduct(@Param("productId") Integer productId);

    /**
     * Lấy danh sách listproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<Product> listproduct(Integer productIds);
    /**
     * Lấy dữ liệu getproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Product getproduct(Integer product_id, Integer campaign_id);
    /**
     * Lấy dữ liệu getproduct1. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    ProductDetail getproduct1(Integer product_id, Integer campaign_id);


}
