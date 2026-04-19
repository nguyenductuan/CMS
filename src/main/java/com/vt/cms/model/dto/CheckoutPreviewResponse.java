package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutPreviewResponse {
    private List<ItemDto> items;
    private List<ShippingMethodDTO> shippingMethods;
    @JsonProperty("total_price")
    private Double totalPrice;

}
