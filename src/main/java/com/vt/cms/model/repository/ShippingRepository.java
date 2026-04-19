package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Shipping;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShippingRepository {
    Shipping detailShipping(String id);

    List<Shipping> getShipping();
}
