package com.vt.cms.model.entity;

import com.vt.cms.model.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Integer id;
    private Double total;
    private OrderStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private List<OrderItem> orderItems;

}
