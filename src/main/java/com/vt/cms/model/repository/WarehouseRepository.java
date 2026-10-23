package com.vt.cms.model.repository;

import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WarehouseRepository {
    List<Warehouse> getlistwahouse();
    Warehouse getdetail(@Param("id") Integer id);
    int add(@Param("warehouse") WarehouseRequest warehouse, @Param("createdAt") LocalDateTime createdAt);
    int setDeleted(@Param("id") Integer id, @Param("deleted") boolean deleted);
    List<WarehouseInventoryResponse> listInventory(@Param("warehouseId") Integer warehouseId);
    List<WarehouseInventoryMovementResponse> listInventoryMovements(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId, @Param("limit") Integer limit);
    int skuExists(@Param("skuId") Integer skuId);
    int ensureInventoryRow(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId);
    int adjustInventory(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId, @Param("delta") Integer delta);
    Integer getInventoryQuantity(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId);
    int insertInventoryMovement(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId,
        @Param("delta") Integer delta, @Param("quantityAfter") Integer quantityAfter,
        @Param("reason") String reason, @Param("createdBy") String createdBy);
}
