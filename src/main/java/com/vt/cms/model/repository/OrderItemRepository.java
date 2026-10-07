package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemRepository {
    void insertorderCartItem(OrderItem orderItem);

    int  insertorderitems(List<OrderItem> orderItem);
}
