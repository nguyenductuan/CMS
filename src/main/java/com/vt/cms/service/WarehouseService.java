package com.vt.cms.service;

import com.vt.cms.model.dto.WarehouseInventoryAdjustRequest;
import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.WarehouseResponse;
import java.util.List;

public interface WarehouseService {
    /**
     * Lấy danh sách listqwarehouse. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<Warehouse> listqwarehouse();
    /**
     * Lấy dữ liệu getdetail. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    WarehouseResponse getdetail(Integer id);
    /**
     * Thêm addwarehouse. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void addwarehouse(WarehouseRequest warehouse);
    /**
     * Thiết lập hoặc cập nhật set warehouse deleted. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void setWarehouseDeleted(Integer id, boolean deleted);
    /**
     * Lấy danh sách list inventory. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<WarehouseInventoryResponse> listInventory(Integer warehouseId);
    /**
     * Điều chỉnh adjust inventory. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void adjustInventory(Integer warehouseId, WarehouseInventoryAdjustRequest request);
    /**
     * Lấy danh sách list inventory movements. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<WarehouseInventoryMovementResponse> listInventoryMovements(Integer warehouseId, Integer skuId, Integer limit);
}
