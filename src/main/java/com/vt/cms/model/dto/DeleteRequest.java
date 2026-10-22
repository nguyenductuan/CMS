package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class DeleteRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than zero")
    private Integer userid;

    @NotEmpty(message = "Product ID list must not be empty")
    private List<@NotNull @Positive(message = "Product ID must be greater than zero") Integer> productids;
}
