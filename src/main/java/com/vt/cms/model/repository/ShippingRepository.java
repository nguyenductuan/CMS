package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingRepository {
    /**
     * Lấy dữ liệu get shipping. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<Shipping> getShipping();

    /**
     * Lấy dữ liệu get shipping by code. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Shipping getShippingByCode(@Param("serviceCode") String serviceCode);
}
