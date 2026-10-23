package com.vt.cms.model.repository;

import com.vt.cms.model.dto.ItemDto;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.order.Itemorderdetail;
import com.vt.cms.model.entity.Order;
import org.apache.ibatis.annotations.Param;
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

    int selectWarehouse(@Param("orderId") Integer orderId,
                        @Param("warehouseCode") String warehouseCode,
                        @Param("updatedAt") java.time.LocalDateTime updatedAt,
                        @Param("updatedBy") String updatedBy);

    int assignShipper(@Param("orderId") Integer orderId,
                      @Param("expectedStatus") String expectedStatus,
                      @Param("nextStatus") String nextStatus,
                      @Param("provider") String provider,
                      @Param("trackingNumber") String trackingNumber,
                      @Param("shipperCode") String shipperCode,
                      @Param("shipperName") String shipperName,
                      @Param("shipperPhone") String shipperPhone,
                      @Param("updatedAt") java.time.LocalDateTime updatedAt,
                      @Param("updatedBy") String updatedBy);

    int markDeliverySuccessful(@Param("orderId") Integer orderId,
                               @Param("trackingNumber") String trackingNumber,
                               @Param("expectedStatus") String expectedStatus,
                               @Param("nextStatus") String nextStatus,
                               @Param("deliveredAt") java.time.LocalDateTime deliveredAt,
                               @Param("updatedBy") String updatedBy);

    int confirmReceived(@Param("orderId") Integer orderId,
                        @Param("expectedStatus") String expectedStatus,
                        @Param("nextStatus") String nextStatus,
                        @Param("receivedAt") java.time.LocalDateTime receivedAt,
                        @Param("updatedBy") String updatedBy);
    // Thanh toan
    List<Itemorderdetail> getOrderItemsByOrderId1(long orderId);
    int cancelOrder(Order order);

    long countorder(OrdersRequest request);

    int completeDeliveredOrdersOlderThan(
            @Param("cutoff") java.time.LocalDateTime cutoff,
            @Param("completedAt") java.time.LocalDateTime completedAt,
            @Param("updatedBy") String updatedBy);
}
