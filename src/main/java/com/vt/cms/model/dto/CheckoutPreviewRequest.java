package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutPreviewRequest {
    private List<ItemRequest> items;
    private String shippingServiceCode;
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
}
