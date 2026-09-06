package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderShipping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderShippingRepository {
    int saveshipment(OrderShipping orderShipping);

    OrderShipping getShipmentByTrackingcode(String trackingcode);
}
