package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CampainProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainProductRepository {
    /**
     * Lưu savecampainproduct. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void savecampainproduct(CampainProduct campainProduct);
    /**
     * Kiểm tra sự tồn tại exists association by product id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    boolean existsAssociationByProductId(@Param("productId") Integer productId);
    /**
     * Thực hiện truy cập dữ liệu deduct stock. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int deductStock( @Param("campainId") Integer campainId,
                     @Param("SkuId") Integer skuId,
                     @Param("quantity") Integer quantity);
}
