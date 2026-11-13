package com.vt.cms.warehouse.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseSelectionRequest {
    @NotBlank(message = "Mã kho không được để trống")
    private String warehouseCode;
}
