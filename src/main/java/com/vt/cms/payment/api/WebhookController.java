package com.vt.cms.payment.api;

import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.payment.application.dto.request.PaymentupdateRequest;
import com.vt.cms.payment.application.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {
  public final PaymentService paymentService;

  // Hàm giả lập thanh toán
  @PostMapping("/test/update")
  public ResponseEntity<APIRessponse> updatePaymentTest(
      @Valid @RequestBody PaymentupdateRequest request) {
    paymentService.updatePaymentFromPartnerTest(request);
    return ResponseEntity.ok(new APIRessponse(200, "Thành công"));
  }
}
