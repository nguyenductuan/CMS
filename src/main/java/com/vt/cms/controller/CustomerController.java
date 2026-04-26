package com.vt.cms.controller;

import com.vt.cms.service.DeliveryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CustomerController {
    final private DeliveryService deliveryService;

    public CustomerController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/confirm")
    public void userconfirm(int orderid) {
        deliveryService.confirmReceived(orderid);
    }
}
