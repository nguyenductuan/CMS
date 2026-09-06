package com.vt.cms.service;

import com.vt.cms.model.dto.OrderShippingRequest;
import com.vt.cms.model.entity.OrderShipping;
import com.vt.cms.model.resp.OrderResponse;

public interface OrderShippingService {
    OrderShipping prepare(OrderShippingRequest orderShippingRequest);

    OrderShipping assginShipper(String trackingcode);

    void shipeperdelivery(int orderid);

    OrderResponse confirmReceived(int orderid);
}
