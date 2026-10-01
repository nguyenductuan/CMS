package com.vt.cms.controller.Buyer;

import com.vt.cms.model.dto.CheckoutPreviewRequest;
import com.vt.cms.model.dto.CheckoutPreviewResponse;
import com.vt.cms.service.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping
@RestController
public class CheckoutController {
@Autowired
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public CheckoutPreviewResponse checkoutController(@RequestBody CheckoutPreviewRequest request) {
        return checkoutService.getCheckout(request);
    }
}
