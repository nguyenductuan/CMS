package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderRepository {
    int insertorder(Order order);

    Order getdetailById(int id);

    void save(Order order);
}
