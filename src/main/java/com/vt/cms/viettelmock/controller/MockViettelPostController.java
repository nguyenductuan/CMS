package com.vt.cms.viettelmock.controller;

import com.vt.cms.viettelmock.dto.MockCreateShipmentRequest;
import com.vt.cms.viettelmock.dto.MockShipmentResponse;
import com.vt.cms.viettelmock.service.MockViettelPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/mock/viettelpost")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "viettel.mock", name = "enabled", havingValue = "true")
public class MockViettelPostController {
    private final MockViettelPostService service;

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public MockShipmentResponse create(@Valid @RequestBody MockCreateShipmentRequest request) {
        return service.create(request);
    }

    @GetMapping("/orders/{orderNumber}")
    public MockShipmentResponse get(@PathVariable String orderNumber) {
        return service.get(orderNumber);
    }
}