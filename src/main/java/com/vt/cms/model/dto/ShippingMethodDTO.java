package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShippingMethodDTO {
    private String id;
    private String name;

    @JsonProperty("estimated_delivery")
    private String estimatedDelivery;

    @JsonProperty("original_fee")
    private int originalFee;

    @JsonProperty("final_fee")
    private int finalFee;

    @JsonProperty("free_shipping_applied")
    private boolean freeShippingApplied;

}
