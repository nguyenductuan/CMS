package com.vt.cms.controller;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.service.PaymentService;
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

    @PostMapping("/paymentorder")
    public void payment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.ProcessPayment(paymentRequest);
    }
}
