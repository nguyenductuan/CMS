package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartRepository {

    /**
     * Thêm bản ghi insert cart. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int insertCart(Cart cart);

    /**
     * Thực hiện truy cập dữ liệu fin by user id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Cart finByUserId(@Param("userId") Integer userId);
}
