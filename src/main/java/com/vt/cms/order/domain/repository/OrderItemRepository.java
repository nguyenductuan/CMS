package com.vt.cms.order.domain.repository;

import com.vt.cms.order.domain.entity.OrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemRepository {

  int saveAll(@Param("orderItems") List<OrderItem> orderItems);

  /**
   * Thêm bản ghi insertorder cart item. Các tham số và giá trị trả về được mô tả bởi chữ ký phương
   * thức.
   */
  void insertorderCartItem(OrderItem orderItem);

  /**
   * Thêm bản ghi insertorderitems. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
  int insertorderitems(List<OrderItem> orderItem);
}
