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

    /**
     * Tạo mới và lưu dữ liệu nghiệp vụ.
     * @param orderId Tham số phục vụ xử lý requestReturn.
     * @param request Tham số phục vụ xử lý requestReturn.
     * @param http Tham số phục vụ xử lý requestReturn.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/{orderId}/return")
    public ResponseEntity<OrderReturnResponse> requestReturn(@PathVariable Integer orderId,
            @Valid @RequestBody CreateReturnRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(service.requestReturn(orderId, request,
                (Integer) http.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE)));
    }

    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @param http Tham số phục vụ xử lý getPending.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping("/return/pending")
    public ResponseEntity<java.util.List<OrderReturnResponse>> getPending(HttpServletRequest http) {
        return ResponseEntity.ok(service.getPending((String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE)));
    }

    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @param returnId Tham số phục vụ xử lý getReturn.
     * @param http Tham số phục vụ xử lý getReturn.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping("/return/{returnId}")
    public ResponseEntity<OrderReturnResponse> getReturn(@PathVariable Integer returnId, HttpServletRequest http) {
        return ResponseEntity.ok(service.getReturn(returnId,
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE)));
    }

    /**
     * Kiểm tra điều kiện và thực hiện bước xét duyệt hoặc xác nhận tương ứng.
     * @param returnId Tham số phục vụ xử lý sellerReview.
     * @param request Tham số phục vụ xử lý sellerReview.
     * @param http Tham số phục vụ xử lý sellerReview.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/return/{returnId}/seller-review")
    public ResponseEntity<OrderReturnResponse> sellerReview(@PathVariable Integer returnId,
            @Valid @RequestBody ReturnReviewRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(service.sellerReview(returnId, request,
                (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE)));
    }

    /**
     * Kiểm tra điều kiện và thực hiện bước xét duyệt hoặc xác nhận tương ứng.
     * @param returnId Tham số phục vụ xử lý adminReview.
     * @param request Tham số phục vụ xử lý adminReview.
     * @param http Tham số phục vụ xử lý adminReview.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/return/{returnId}/select-method")
    public ResponseEntity<OrderReturnResponse> selectReturnMethod(@PathVariable Integer returnId,
            @RequestBody java.util.Map<String, String> body, HttpServletRequest http) {
        return ResponseEntity.ok(service.selectReturnMethod(returnId, body.get("returnMethod"),
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
