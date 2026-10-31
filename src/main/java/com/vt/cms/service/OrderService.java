package com.vt.cms.service;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderCreateResponse;
import com.vt.cms.model.resp.OrderResponse;

import java.util.List;

public interface OrderService {
    /**
     * Lấy dữ liệu getorderlist. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request);

    /**
     * Tạo mới dữ liệu create order. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    OrderCreateResponse createOrder(OrderRequest request, String buyerUsername);

    /**
     * Lấy dữ liệu getdetailorder. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    OrderResponse getdetailorder(long id);

    /**
     * Hủy nghiệp vụ cancel order. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    void cancelOrder(int orderId, String notecancel, String actorUsername, String actorRole);

}
