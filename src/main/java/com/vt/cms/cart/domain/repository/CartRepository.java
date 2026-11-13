package com.vt.cms.cart.domain.repository;

import com.vt.cms.cart.domain.entity.Cart;
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
