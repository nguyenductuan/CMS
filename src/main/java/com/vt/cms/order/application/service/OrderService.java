package com.vt.cms.order.application.service;

public interface OrderService {
    PreparedOrderResponse preparedOrder(PreparedOrderRequest request, boolean isSellerOrder);
}
