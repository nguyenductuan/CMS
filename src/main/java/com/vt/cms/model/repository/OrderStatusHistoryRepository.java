package com.vt.cms.model.repository;

import com.vt.cms.model.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderStatusHistoryRepository {
    int insertorderByStatus(Integer orderId, OrderStatus status);
}
