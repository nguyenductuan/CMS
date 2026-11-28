package com.vt.cms.payment.api;

import com.vt.cms.payment.application.dto.request.PaymentRequest;
import com.vt.cms.payment.application.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/b/payment")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  // Gọi API payment từ FE, trả về thông tin thanh toán
  @PostMapping("/payment")
  public void createdpayment(@RequestBody PaymentRequest request) {
    paymentService.creatpayment(request);
  }
}
