// package com.vt.cms.checkout.api;
//
// import com.vt.cms.checkout.application.dto.request.CheckoutPreviewRequest;
// import com.vt.cms.checkout.application.dto.response.CheckoutPreviewResponse;
// import com.vt.cms.checkout.application.service.CheckoutService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @Slf4j
// @RequestMapping
// @RestController
// public class CheckoutController {
//  @Autowired private final CheckoutService checkoutService;
//
//  public CheckoutController(CheckoutService checkoutService) {
//    this.checkoutService = checkoutService;
//  }
//
//  @PostMapping("/checkout")
//  /**
//   * Kiểm tra điều kiện nghiệp vụ checkout controller; nhận các tham số khai báo trong chữ ký hàm
// và
//   * trả về kết quả theo kiểu dữ liệu đã định nghĩa.
//   */
//  public CheckoutPreviewResponse checkoutController(@RequestBody CheckoutPreviewRequest request) {
//    return checkoutService.getCheckout(request);
//  }
// }
