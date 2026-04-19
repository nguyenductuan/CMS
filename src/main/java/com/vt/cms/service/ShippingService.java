package com.vt.cms.service;

import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.resp.ShippingResponse;

import java.util.List;

public interface ShippingService {
    List<Shipping> getShipping();

    ShippingResponse getDetailshipping(String id);
}
