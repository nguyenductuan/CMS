package com.vt.cms.order.api;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.request.CreateOrderRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;
import com.vt.cms.order.application.dto.response.OrderCreateResponse;
import com.vt.cms.order.application.dto.response.OrderResponse;
import com.vt.cms.order.application.service.BuyerOrderService;
import com.vt.cms.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/b/order")
@SecurityRequirement(name = "bearerAuth")
public class BuyerOrderController {
  final BuyerOrderService buyerOrderService;

  public BuyerOrderController(BuyerOrderService buyerOrderService) {
    this.buyerOrderService = buyerOrderService;
  }

  @PostMapping("/checkoutpreview")
  public ResponseEntity<APIRessponse> checkoutController(
      @RequestBody CheckoutPreviewRequest request) {
    CheckoutPreviewResponse checkoutPreviewResponse = buyerOrderService.checkoutPreview(request);
    return ResponseEntity.ok(new APIRessponse(200, "Thành công", checkoutPreviewResponse));
  }

  // Ham tao order
  @PostMapping("createOrder")
  public ResponseEntity<APIRessponse> order(
      @Valid @RequestBody CreateOrderRequest orderRequest, HttpServletRequest request) {
    Integer userId = (Integer) request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    OrderCreateResponse orderCreateResponse = buyerOrderService.createOrder(orderRequest, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Thành công", orderCreateResponse));
  }

  // Danh sách đơn hàng
  @GetMapping("order_list")
  public BaseResponse<PagingResponse<List<OrderResponse>>> order_list(
      @ModelAttribute Pagerequest pagerequest) {
    return buyerOrderService.listOrder(pagerequest);
  }
}
