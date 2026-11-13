package com.vt.cms.warehouse.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseStatusRequest {
    @NotNull private Integer id;
    @NotNull private Boolean active;
}
