package com.vt.cms.service;

import com.vt.cms.model.entity.Shipping;

import java.util.List;

public interface ShippingService {
    /**
     * Lấy dữ liệu get shipping. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<Shipping> getShipping();

}
