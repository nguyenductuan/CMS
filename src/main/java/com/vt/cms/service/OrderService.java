package com.vt.cms.service;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.resp.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getorderlist();

    void createOrder(OrderRequest request);

    OrderResponse getdetailorder(long id);

    void cancelOrder(int orderId);

}
