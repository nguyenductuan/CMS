package com.vt.cms.service;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.PaymentCallback;
import com.vt.cms.model.resp.PaymentResponse;

public interface PaymentService {
    void paymentCallback(PaymentCallback paymentCallback);
    PaymentResponse payment(PaymentRequest paymentRequest);
}
