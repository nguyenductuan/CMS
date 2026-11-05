package com.vt.cms.order.application.service;

import com.vt.cms.order.application.dto.PreparedOrderRequest;

public interface OrderService {
    void preparedOrder(PreparedOrderRequest request, boolean isSellerOrder);
}
