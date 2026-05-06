package com.vt.cms.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TrackingOrder {
    @PostMapping("tracking/{orderId}")
    public void TrackingOrder(@PathVariable Integer orderId) {

    }
}
