package com.vt.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class ItemRequest {
    @JsonProperty("product_id")
    private String productId;
    private  int quantity;

}
