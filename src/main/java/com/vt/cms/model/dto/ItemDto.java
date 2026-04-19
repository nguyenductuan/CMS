package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDto {
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_name")
    private String productName;
    private Double price;
    private int quantity;
    private int stock;

}
