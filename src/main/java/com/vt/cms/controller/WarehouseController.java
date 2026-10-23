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
     * Lấy lịch sử biến động tồn kho của kho, có thể lọc theo SKU và giới hạn số bản ghi trả về.
     */
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(
            @PathVariable Integer warehouseId, @RequestParam(required = false) Integer skuId,
            @RequestParam(required = false) Integer limit) {
        return warehouseService.listInventoryMovements(warehouseId, skuId, limit);
    }
}
