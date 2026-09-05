package com.vt.cms.controller;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.resp.APIRessponse;
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

    @PostMapping("/paymentorder")
    public ResponseEntity<APIRessponse> paymentwebhooks(@RequestBody PaymentRequest paymentRequest) {
        paymentService.Paymentwebhooks(paymentRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Successfully", errors));

    }
}
