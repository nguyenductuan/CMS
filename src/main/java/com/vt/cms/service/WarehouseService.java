package com.vt.cms.service;

import com.vt.cms.model.dto.WarehouseInventoryAdjustRequest;
import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.WarehouseResponse;
import java.util.List;

public interface WarehouseService {
    List<Warehouse> listqwarehouse();
    WarehouseResponse getdetail(Integer id);
    void addwarehouse(WarehouseRequest warehouse);
    void setWarehouseDeleted(Integer id, boolean deleted);
    List<WarehouseInventoryResponse> listInventory(Integer warehouseId);
    void adjustInventory(Integer warehouseId, WarehouseInventoryAdjustRequest request);
    List<WarehouseInventoryMovementResponse> listInventoryMovements(Integer warehouseId, Integer skuId, Integer limit);
}
