package com.vt.cms.payment.application.service;

import com.vt.cms.payment.application.dto.request.PaymentRequest;
import com.vt.cms.payment.application.dto.request.PaymentupdateRequest;

public interface PaymentService {
  void creatpayment(PaymentRequest request);

  void updatePaymentFromPartnerTest(PaymentupdateRequest request);
}
