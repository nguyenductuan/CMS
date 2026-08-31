package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutPreviewResponse {
    private List<ItemDto> items;
    private List<ShippingMethodDTO> shippingMethods;
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    private Double shipping_fee; // xem lại có cần không
    private Double subtotal;
    private String paymentMethod;
}
