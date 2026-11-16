package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> order;
    private Double totalamount;
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
}
