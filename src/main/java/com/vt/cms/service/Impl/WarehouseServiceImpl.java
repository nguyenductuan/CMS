package com.vt.cms.service.Impl;

import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.repository.WarehouseRepository;
import com.vt.cms.model.resp.WarehouseResponse;
import com.vt.cms.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<Warehouse> listqwarehouse() {
        return warehouseRepository.getlistwahouse();
    }

    @Override
    public WarehouseResponse getdetail(Integer id) {
        Warehouse warehouse = warehouseRepository.getdetail(id);
        WarehouseResponse response = new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setWarehouse_code(warehouse.getWarehouse_code());
        response.setWarehouse_name(warehouse.getWarehouse_name());
        return response;
    }
}
