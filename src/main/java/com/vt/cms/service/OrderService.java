package com.vt.cms.service;

import com.vt.cms.model.dto.OrderRequest;

public interface OrderService {
    void createOrder(OrderRequest request);
}
