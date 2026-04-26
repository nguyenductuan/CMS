package com.vt.cms.controller;

import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.service.WarehouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
