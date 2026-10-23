package com.vt.cms.controller;

import com.vt.cms.model.dto.WarehouseInventoryAdjustRequest;
import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.dto.WarehouseStatusRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.WarehouseResponse;
import com.vt.cms.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping({"/warehouse", "/warehouse/"})
    public List<Warehouse> getWarehouse() {
        return warehouseService.listqwarehouse();
    }

    @GetMapping("/warehouse/{id}")
    public WarehouseResponse getWarehouse(@PathVariable Integer id) {
        return warehouseService.getdetail(id);
    }

    @PostMapping("/warehouse")
    public ResponseEntity<APIRessponse> createWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Tạo kho thành công"));
    }

    @PostMapping({"/addwarehouse", "/warehouse/addwarehouse"})
    public ResponseEntity<APIRessponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Thêm kho thành công"));
    }

    @DeleteMapping("/warehouse/{id}")
    public ResponseEntity<APIRessponse> deleteWarehouse(@PathVariable Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    @PostMapping({"/deletewarehouse", "/warehouse/deletewarehouse"})
    public ResponseEntity<APIRessponse> deleteWarehouseLegacy(@RequestBody Warehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new IllegalArgumentException("Mã kho không được để trống");
        }
        warehouseService.setWarehouseDeleted(warehouse.getId(), true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    @PutMapping("/warehouse/status")
    public ResponseEntity<APIRessponse> updateWarehouseStatus(@Valid @RequestBody WarehouseStatusRequest request) {
        warehouseService.setWarehouseDeleted(request.getId(), !request.getActive());
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    @PostMapping({"/statuswarehouse", "/warehouse/statuswarehouse"})
    public ResponseEntity<APIRessponse> statusWarehouse(@RequestBody Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    @GetMapping("/warehouse/{warehouseId}/inventory")
    public List<WarehouseInventoryResponse> listInventory(@PathVariable Integer warehouseId) {
        return warehouseService.listInventory(warehouseId);
    }

    @PostMapping("/warehouse/{warehouseId}/inventory/adjust")
    public ResponseEntity<APIRessponse> adjustInventory(@PathVariable Integer warehouseId,
            @Valid @RequestBody WarehouseInventoryAdjustRequest request) {
        warehouseService.adjustInventory(warehouseId, request);
        return ResponseEntity.ok(new APIRessponse(200, "Điều chỉnh tồn kho thành công"));
    }

    @GetMapping("/warehouse/{warehouseId}/inventory/movements")
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(
            @PathVariable Integer warehouseId, @RequestParam(required = false) Integer skuId,
            @RequestParam(required = false) Integer limit) {
        return warehouseService.listInventoryMovements(warehouseId, skuId, limit);
    }
}
