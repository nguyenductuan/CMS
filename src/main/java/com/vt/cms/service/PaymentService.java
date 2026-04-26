package com.vt.cms.service;

import com.vt.cms.model.dto.PaymentRequest;

public interface PaymentService {
    void ProcessPayment(PaymentRequest paymentRequest);

}
