package com.vt.cms.service;

import com.vt.cms.model.entity.Shipment;
import com.vt.cms.model.resp.OrderResponse;

public interface DeliveryService {
    Shipment prepare(Integer orderid, Integer warehousecode);

    Shipment assginShipper(String trackingcode);

    void shipeperdelivery(int orderid);

    OrderResponse confirmReceived(int orderid);
}
