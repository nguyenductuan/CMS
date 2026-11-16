package com.vt.cms.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WarehouseInventoryMovementResponse {
    private Integer id;
    private Integer warehouseId;
    private Integer skuId;
    private String skuCode;
    private Integer quantityDelta;
    private Integer quantityAfter;
    private String reason;
    private String createdBy;
    private LocalDateTime createdAt;
}
