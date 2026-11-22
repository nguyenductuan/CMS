package com.vt.cms.order.application.service;

import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;

public interface BuyerOrderService {

  CheckoutPreviewResponse checkoutPreview(CheckoutPreviewRequest request);
}
