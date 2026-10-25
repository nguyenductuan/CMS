package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemRepository {
    /**
     * Thêm bản ghi insertorder cart item. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void insertorderCartItem(OrderItem orderItem);

    /**
     * Thêm bản ghi insertorderitems. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int  insertorderitems(List<OrderItem> orderItem);
}
