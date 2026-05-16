package com.vt.cms.service;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderResponse;

import java.util.List;

public interface OrderService {
    BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request);

    void createOrder(OrderRequest request);

    OrderResponse getdetailorder(long id);

    void cancelOrder(int orderId, String notecancel);

}
