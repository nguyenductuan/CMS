package com.vt.cms.model.resp;

import com.vt.cms.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer orderid;
    private Integer total;
    private OrderStatus OrderStatus;
    private LocalDateTime ordercreated;
    private LocalDateTime deliveredAt;
    private List<OrderItemResponse> orderItems;
}
