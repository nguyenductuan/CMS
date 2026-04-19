package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> orderItems;
    private Double totalPrice;
    private String PaymentMethod;
    private String address;
    private String note;
    private String ShippingMethodId;
}

