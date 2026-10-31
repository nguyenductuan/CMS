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
    /**
     * Thêm bản ghi insertorder. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int insertorder(Order order);

    /**
     * Lấy dữ liệu getorderbyid. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    OrderResponse getorderbyid(long orderId);

    /**
     * Lấy dữ liệu getorderby id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Order getorderbyId(Integer orderId);
    Order getorderbyIdForUpdate(@Param("orderId") Integer orderId);
    List<OrderResponse> getOrder(OrdersRequest request);

    /**
     * Lấy dữ liệu get items by order id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<OrderItemResponse> getItemsByOrderId(long orderId);
    /**
     * Lấy dữ liệu get order items by order id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<ItemDto> getOrderItemsByOrderId(long orderId);
    /**
     * Lưu save. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void save(Order order);

    /**
     * Truy vấn select warehouse. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int selectWarehouse(@Param("orderId") Integer orderId,
                        @Param("warehouseCode") String warehouseCode,
                        @Param("updatedAt") java.time.LocalDateTime updatedAt,
                        @Param("updatedBy") String updatedBy);

    /**
     * Gán dữ liệu assign shipper. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
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

    /**
     * Thực hiện truy cập dữ liệu mark delivery successful. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int markDeliverySuccessful(@Param("orderId") Integer orderId,
                               @Param("trackingNumber") String trackingNumber,
                               @Param("expectedStatus") String expectedStatus,
                               @Param("nextStatus") String nextStatus,
                               @Param("deliveredAt") java.time.LocalDateTime deliveredAt,
                               @Param("updatedBy") String updatedBy);

    /**
     * Xác nhận confirm received. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int confirmReceived(@Param("orderId") Integer orderId,
                        @Param("expectedStatus") String expectedStatus,
                        @Param("nextStatus") String nextStatus,
                        @Param("receivedAt") java.time.LocalDateTime receivedAt,
                        @Param("updatedBy") String updatedBy);
    // Thanh toan
    /**
     * Lấy dữ liệu get order items by order id1. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<Itemorderdetail> getOrderItemsByOrderId1(long orderId);
    int cancelOrder(Order order);
    int setReturnRequested(@Param("orderId") Integer orderId, @Param("buyerUsername") String buyerUsername, @Param("updatedAt") java.time.LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    int setReturnRejected(@Param("orderId") Integer orderId, @Param("updatedAt") java.time.LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    int updateReturnSubStatus(@Param("orderId") Integer orderId, @Param("status") String status, @Param("updatedBy") String updatedBy, @Param("updatedAt") java.time.LocalDateTime updatedAt);
    int escalateReturnReviews(@Param("cutoff") java.time.LocalDateTime cutoff, @Param("updatedAt") java.time.LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);

    long countorder(OrdersRequest request);

    /**
     * Hoàn tất complete delivered orders older than. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int completeDeliveredOrdersOlderThan(
            @Param("cutoff") java.time.LocalDateTime cutoff,
            @Param("completedAt") java.time.LocalDateTime completedAt,
            @Param("updatedBy") String updatedBy);
}
