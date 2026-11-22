package com.vt.cms.order.api;

import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;
import com.vt.cms.order.application.service.BuyerOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("v1/b/order")
@SecurityRequirement(name = "bearerAuth")
@RestController
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
}
