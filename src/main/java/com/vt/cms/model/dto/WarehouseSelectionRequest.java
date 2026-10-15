package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseSelectionRequest {
    @NotBlank(message = "Mã kho không được để trống")
    private String warehouseCode;
}
