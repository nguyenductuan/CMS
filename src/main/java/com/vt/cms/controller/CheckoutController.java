package com.vt.cms.controller;

import com.vt.cms.model.dto.CheckoutPreviewRequest;
import com.vt.cms.model.dto.CheckoutPreviewResponse;
import com.vt.cms.service.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public CheckoutPreviewResponse checkoutController(@RequestBody CheckoutPreviewRequest request) {
        return checkoutService.getCheckout(request);
    }
}
