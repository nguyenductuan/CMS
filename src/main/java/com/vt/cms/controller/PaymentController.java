package com.vt.cms.controller;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.enums.PaymentStatus;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.PaymentResponse;
import com.vt.cms.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    // Gọi API payment từ FE, trả về thông tin thanh toán
    @PostMapping("/paymentorder")
    /**
     * Xử lý thanh toán payment. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> payment(@RequestBody PaymentRequest paymentRequest) {
      PaymentResponse paymentResponse=  paymentService.payment(paymentRequest);
        return ResponseEntity.ok(new APIRessponse(200, PaymentStatus.PAYMENT_SUCCESS.getMessage(), paymentResponse));
    }
    // Gọi API callback từ cổng thanh toán, cập nhật trạng thái thanh toán
    @PostMapping("/paymentcallback")
    /**
     * Xử lý thanh toán paymentcallback. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> paymentcallback(@RequestBody Paymentcallback paymentcallback) {
        paymentService.paymentcallback(paymentcallback);
        return ResponseEntity.ok(new APIRessponse(200, PaymentStatus.PAYMENT_SUCCESS.getMessage()));
    }
}
