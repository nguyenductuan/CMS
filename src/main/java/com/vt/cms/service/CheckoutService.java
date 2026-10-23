package com.vt.cms.service;

import com.vt.cms.model.dto.CheckoutPreviewRequest;
import com.vt.cms.model.dto.CheckoutPreviewResponse;

public interface CheckoutService {
    /**
     * Lấy dữ liệu get checkout. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    CheckoutPreviewResponse getCheckout(CheckoutPreviewRequest request);
}
