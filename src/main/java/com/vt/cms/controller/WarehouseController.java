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
    /**
     * Lấy dữ liệu get warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public List<Warehouse> getWarehouse() {
        return warehouseService.listqwarehouse();
    }

    @GetMapping("/warehouse/{id}")
    /**
     * Lấy dữ liệu get warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public WarehouseResponse getWarehouse(@PathVariable Integer id) {
        return warehouseService.getdetail(id);
    }

    @PostMapping("/warehouse")
    /**
     * Tạo mới dữ liệu create warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> createWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Tạo kho thành công"));
    }

    @PostMapping({"/addwarehouse", "/warehouse/addwarehouse"})
    /**
     * Thêm dữ liệu add warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Thêm kho thành công"));
    }

    @DeleteMapping("/warehouse/{id}")
    /**
     * Xóa dữ liệu delete warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> deleteWarehouse(@PathVariable Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    @PostMapping({"/deletewarehouse", "/warehouse/deletewarehouse"})
    /**
     * Xóa dữ liệu delete warehouse legacy. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> deleteWarehouseLegacy(@RequestBody Warehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new IllegalArgumentException("Mã kho không được để trống");
        }
        warehouseService.setWarehouseDeleted(warehouse.getId(), true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    @PutMapping("/warehouse/status")
    /**
     * Cập nhật dữ liệu update warehouse status. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> updateWarehouseStatus(@Valid @RequestBody WarehouseStatusRequest request) {
        warehouseService.setWarehouseDeleted(request.getId(), !request.getActive());
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    @PostMapping({"/statuswarehouse", "/warehouse/statuswarehouse"})
    /**
     * Thực hiện xử lý nghiệp vụ status warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> statusWarehouse(@RequestBody Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    @GetMapping("/warehouse/{warehouseId}/inventory")
    /**
     * Lấy danh sách list inventory. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public List<WarehouseInventoryResponse> listInventory(@PathVariable Integer warehouseId) {
        return warehouseService.listInventory(warehouseId);
    }

    @PostMapping("/warehouse/{warehouseId}/inventory/adjust")
    /**
     * Thực hiện xử lý nghiệp vụ adjust inventory. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> adjustInventory(@PathVariable Integer warehouseId,
            @Valid @RequestBody WarehouseInventoryAdjustRequest request) {
        warehouseService.adjustInventory(warehouseId, request);
        return ResponseEntity.ok(new APIRessponse(200, "Điều chỉnh tồn kho thành công"));
    }

    @GetMapping("/warehouse/{warehouseId}/inventory/movements")
    /**
     * Lấy danh sách list inventory movements. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(
            @PathVariable Integer warehouseId, @RequestParam(required = false) Integer skuId,
            @RequestParam(required = false) Integer limit) {
        return warehouseService.listInventoryMovements(warehouseId, skuId, limit);
    }
}
