package com.vt.cms.service;

import com.vt.cms.model.dto.CheckoutPreviewRequest;
import com.vt.cms.model.dto.CheckoutPreviewResponse;

public interface CheckoutService {
    CheckoutPreviewResponse getCheckout(CheckoutPreviewRequest request);
}
