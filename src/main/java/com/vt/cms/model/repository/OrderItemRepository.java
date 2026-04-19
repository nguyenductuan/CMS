package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemRepository {
    void insertorderCartItem(OrderItem orderItem);
}
