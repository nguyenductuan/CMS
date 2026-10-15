package com.vt.cms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than zero")
    private Integer userId;

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be greater than zero")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;
}
