package com.vt.cms.order.domain.entity;

import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Order {
  private Integer id;
  private BigDecimal totalPrice;
  private BigDecimal productweight;
  private BigDecimal productlength;
  private BigDecimal productwidth;
  private BigDecimal productheight;
  private OrderStatus paymentStatus;
  private LocalDateTime createdAt;
  private LocalDateTime deliveredAt;
  private LocalDateTime cancelAt;
  private OrderSubStatus subOrderStatus;
  private LocalDateTime expected_delivery;
  private String warehouseCode;
  private String shippingProvider;
  private String trackingNumber;
  private Integer productId;
  private Integer totalQuantity;
  private Integer campaignId;
  private Integer totalWeight;
  private BigDecimal totalPayment; // Tổng tiền thanh toán
  private BigDecimal shippingFee; // phí vận chuyển đơn hàng

  /** Mã trạng thái vận chuyển đối tác, tách biệt với trạng thái nghiệp vụ order_status. */
  private String shippingStatusCode;

  private String shipperCode;
  private String shipperName;
  private String shipperPhone;
  private LocalDateTime shippedAt;
  private LocalDateTime carrierDeliveredAt;
  private LocalDateTime customerReceivedAt;
  private String createdBy;
  private String cancelledBy;
  private String isDeleted;
  private String notecancel;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private OrderSubStatus orderSubStatus;

  private LocalDateTime createAt;
  private LocalDateTime updatedAt;
  private String updated_by;
  private List<OrderItem> orderItems;
  private LocalDateTime expectedDelivery;
  private Double DeliveryLatitude;
  private Double DeliveryLongitude;
}
