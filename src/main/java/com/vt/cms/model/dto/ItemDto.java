package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {
    @JsonProperty("product_id")
    private int productId;
    @JsonProperty("product_name")
    private String productName;
    private Integer campainID;
    private BigDecimal price;
    private  BigDecimal price_campain;
    private BigDecimal price_discount;
    private int quantity;
    private int stock;

}
