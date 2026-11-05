package com.vt.cms.order.application.service;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderCreateResponse;
import com.vt.cms.model.resp.OrderResponse;
import java.util.List;
/** Application boundary for order use cases. */
public interface OrderUseCase {
 BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request);
 OrderCreateResponse createOrder(OrderRequest request,String buyerUsername);
 OrderResponse getdetailorder(long id);
 void cancelOrder(int orderId,String notecancel,String actorUsername,String actorRole);
}