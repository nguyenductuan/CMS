package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutPreviewResponse {
    private List<ItemDto> items;
    private BigDecimal totalPrice;
    private BigDecimal shipping_fee;
    private String paymentMethod;
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
}
