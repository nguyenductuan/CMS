package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingRepository {
    List<Shipping> getShipping();

    Shipping getShippingByCode(@Param("serviceCode") String serviceCode);
}
