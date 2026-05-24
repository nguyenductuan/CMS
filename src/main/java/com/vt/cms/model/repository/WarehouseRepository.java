package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseRepository {
    List<Warehouse> getlistwahouse();

    Warehouse getdetail(Integer id);
}
