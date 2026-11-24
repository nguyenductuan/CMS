package com.vt.cms.order.application.service;

import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.request.CreateOrderRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;
import com.vt.cms.order.application.dto.response.OrderCreateResponse;

public interface BuyerOrderService {

  CheckoutPreviewResponse checkoutPreview(CheckoutPreviewRequest request);

  OrderCreateResponse createOrder(CreateOrderRequest orderRequest, Integer userId);
}
