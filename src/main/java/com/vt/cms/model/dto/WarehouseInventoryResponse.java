package com.vt.cms.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WarehouseInventoryResponse {
    private Integer warehouseId;
    private String warehouseName;
    private Integer skuId;
    private String skuCode;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private LocalDateTime updatedAt;
}
