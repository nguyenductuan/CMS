package com.vt.cms.service;

import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> listqwarehouse();

    WarehouseResponse getdetail(Integer id);

    void addwarehouse(WarehouseRequest warehouse);
}
