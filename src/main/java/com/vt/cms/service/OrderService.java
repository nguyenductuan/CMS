package com.vt.cms.service;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.entity.Order;

public interface OrderService {
    void createOrder(OrderRequest request);

    Order getdetailorder(int id);
    
}
