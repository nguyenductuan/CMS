package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WarehouseInventoryAdjustRequest {
    @NotNull @Positive
    private Integer skuId;
    /** Positive adds stock; negative removes stock. */
    @NotNull
    private Integer quantityDelta;
    private String reason;
}
