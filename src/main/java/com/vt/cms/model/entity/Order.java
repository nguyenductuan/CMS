package com.vt.cms.model.entity;

import com.vt.cms.model.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Integer id;
    private BigDecimal total;
    private BigDecimal productweight;
    private BigDecimal productlength;
    private BigDecimal productwidth;
    private BigDecimal productheight;
    private OrderStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelAt;
    private OrderStatus substatus;
    private LocalDateTime expected_delivery;

    private  String created_by;
    private String notecancel;
    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;
    private List<OrderItem> orderItems;
    private LocalDateTime expectedDelivery;
    private  Double DeliveryLatitude;
    private  Double DeliveryLongitude;

}
