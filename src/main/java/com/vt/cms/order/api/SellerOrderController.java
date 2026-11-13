package com.vt.cms.order.api;

import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.order.application.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/s/v1/order")
@RequiredArgsConstructor
@Slf4j
public class SellerOrderController {
    public final OrderService orderService;
    @PutMapping("/prepared")
    /**
     * Chẩn bị đơn hàng
     */
    public ResponseEntity<APIRessponse> preparedOrder(@PathVariable Integer orderId,@Valid @RequestBody PreparedOrderRequest1 request){
        log.info("API prepared order. orderId={}", orderId);
        orderService.preparedOrder(request, true);
        return ResponseEntity.ok(new APIRessponse(200, "Đã chọn kho, đơn hàng đang chuẩn bị"));
    }

}
