package com.vt.cms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class AddCartRequest {
    private Integer userId;
    @Null(message = "Product ID must be null when adding to cart")
    private Integer productId;
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;
}
