package com.vt.cms.payment.application.service;
import com.vt.cms.model.dto.PaymentCallback;
import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.resp.PaymentResponse;
/** Application boundary for payment creation and provider callbacks. */
public interface PaymentUseCase {
 void paymentCallback(PaymentCallback paymentCallback);
 PaymentResponse payment(PaymentRequest paymentRequest);
}