package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Product_Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductSkuRepository {
    /**
     * Thực hiện truy cập dữ liệu inserproductsku. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int inserproductsku(Product_Sku productSku);
    /**
     * Tìm kiếm find by product id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<Product_Sku> findByProductId(Integer product_id);
    /**
     * Tìm kiếm find not deleted by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Product_Sku findNotDeletedById(@Param("skuId") Integer skuId);
    /**
     * Thực hiện truy cập dữ liệu soft delete by product id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int softDeleteByProductId(@Param("productId") Integer productId);
    /**
     * Cập nhật update by product id and sku code. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int updateByProductIdAndSkuCode(@Param("sku") Product_Sku sku);
}
