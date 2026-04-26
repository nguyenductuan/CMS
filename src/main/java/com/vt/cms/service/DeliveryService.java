package com.vt.cms.service;

import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Shipment;

public interface DeliveryService {
    Shipment prepare(Integer orderid, Integer warehousecode);

    Shipment assginShipper(String trackingcode);

    void shipeperdelivery(int orderid);

    Order confirmReceived(int orderid);
}
