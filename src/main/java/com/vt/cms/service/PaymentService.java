package com.vt.cms.service;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.repository.PaymentRepostitory;
import com.vt.cms.model.resp.PaymentResponse;

public interface PaymentService {

    void paymentcallback(Paymentcallback paymentcallback);
    PaymentResponse payment(PaymentRequest paymentRequest);

}
