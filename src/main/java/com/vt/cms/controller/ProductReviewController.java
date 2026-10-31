package com.vt.cms.controller;

import com.vt.cms.model.dto.CreateReviewRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.security.JwtAuthenticationFilter;
import com.vt.cms.service.ProductReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService reviewService;

    /**
     * Tạo mới và lưu dữ liệu nghiệp vụ.
     * @param request Tham số phục vụ xử lý createReview.
     * @param httpRequest Tham số phục vụ xử lý createReview.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/review")
    public ResponseEntity<APIRessponse> createReview(@Valid @RequestBody CreateReviewRequest request,
                                                      HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
        String username = (String) httpRequest.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE);
        return ResponseEntity.ok(new APIRessponse(200, "Đánh giá sản phẩm thành công",
                reviewService.createReview(request, userId, username)));
    }
}
