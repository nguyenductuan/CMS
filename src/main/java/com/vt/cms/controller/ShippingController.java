package com.vt.cms.controller;

import com.vt.cms.service.ShippingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ShippingController {

    final private ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("list_shipping")
    public void list_shipping() {
        shippingService.getShipping();

    }

    @GetMapping("/shipping/{id}")
    public void getdetailshipping(@PathVariable String id) {
        shippingService.getDetailshipping(id);

    }
}
