package com.vt.cms.model.repository;

import com.vt.cms.model.dto.ItemDto;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.resp.OrderItemResponse;
import com.vt.cms.model.resp.OrderResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRepository {
    int insertorder(Order order);

    OrderResponse getorderbyid(long orderId);

    Order getorderbyId(Integer orderId);
    List<OrderResponse> getOrder(OrdersRequest request);

    List<OrderItemResponse> getItemsByOrderId(long orderId);
    List<ItemDto> getOrderItemsByOrderId(long orderId);
    void save(Order order);

    void cancelOrder(Order order);

    long countorder();
}
