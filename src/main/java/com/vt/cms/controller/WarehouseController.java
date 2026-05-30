package com.vt.cms.controller;

import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.WarehouseResponse;
import com.vt.cms.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/warehouse")
    public List<Warehouse> getWarehouse() {
        return warehouseService.listqwarehouse();
    }

    @GetMapping("warehouse/{id}")
    public WarehouseResponse getWarehouse(@PathVariable Integer id) {
        return warehouseService.getdetail(id);
    }

    @PostMapping("addwarehouse")
    public void addWarehouse(@RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);

    }

    @PostMapping("deletewarehouse")
    public void deleteWarehouse(@RequestBody Warehouse warehouse) {
    }

    @PostMapping("statuswarehouse")
    public void statusWarehouse(@RequestBody Integer id) {

    }
}
