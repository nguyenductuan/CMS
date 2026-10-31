package com.vt.cms.controller;

import com.vt.cms.model.dto.CreateReturnRequest;
import com.vt.cms.model.dto.ReturnReviewRequest;
import com.vt.cms.model.resp.OrderReturnResponse;
import com.vt.cms.security.JwtAuthenticationFilter;
import com.vt.cms.service.OrderReturnService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderReturnController {
    private final OrderReturnService service;

    @PostMapping("/{orderId}/return")
    public ResponseEntity<OrderReturnResponse> requestReturn(@PathVariable Integer orderId,
            @Valid @RequestBody CreateReturnRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(service.requestReturn(orderId, request,
                (Integer) http.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE)));
    }

    @GetMapping("/return/{returnId}")
    public ResponseEntity<OrderReturnResponse> getReturn(@PathVariable Integer returnId) {
        return ResponseEntity.ok(service.getReturn(returnId));
    }

    @PostMapping("/return/{returnId}/seller-review")
    public ResponseEntity<OrderReturnResponse> sellerReview(@PathVariable Integer returnId,
            @Valid @RequestBody ReturnReviewRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(service.sellerReview(returnId, request,
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE)));
    }

    @PostMapping("/return/{returnId}/admin-review")
    public ResponseEntity<OrderReturnResponse> adminReview(@PathVariable Integer returnId,
            @Valid @RequestBody ReturnReviewRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(service.adminReview(returnId, request,
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE)));
    }
}
