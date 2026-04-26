package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Shipment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShipmentRepository {
    int saveshipment(Shipment shipment);

    Shipment getShipmentByTrackingcode(String trackingcode);
}
