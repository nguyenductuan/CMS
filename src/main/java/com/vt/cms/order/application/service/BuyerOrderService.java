package com.vt.cms.order.application.service;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.request.CreateOrderRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;
import com.vt.cms.order.application.dto.response.OrderCreateResponse;
import com.vt.cms.order.application.dto.response.OrderResponse;
import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import java.util.List;

public interface BuyerOrderService {

  void uploadOrderStatus(
      List<Integer> orderId, OrderStatus orderStatus, OrderSubStatus orderSubStatus);

  BaseResponse<PagingResponse<List<OrderResponse>>> listOrder(Pagerequest pagerequest);

  CheckoutPreviewResponse checkoutPreview(CheckoutPreviewRequest request);

  OrderCreateResponse createOrder(CreateOrderRequest orderRequest, Integer userId);

  List<OrderResponse> getOrderByIds(List<Integer> orderIds);
}
