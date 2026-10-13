package com.vt.cms.controller;

import com.vt.cms.model.dto.WarehouseSelectionRequest;
import com.vt.cms.model.entity.ShippingAssignment;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.OrderFulfillmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderFulfillmentController {
    private final OrderFulfillmentService fulfillmentService;

    @PostMapping("/{orderId}/warehouse")
    public ResponseEntity<APIRessponse> selectWarehouse(
            @PathVariable Integer orderId,
            @Valid @RequestBody WarehouseSelectionRequest request) {
        log.info("API select warehouse. orderId={}", orderId);
        fulfillmentService.selectWarehouse(orderId, request);
        return ResponseEntity.ok(new APIRessponse(200, "Đã chọn kho, đơn hàng đang chuẩn bị"));
    }

    @PostMapping("/{orderId}/dispatch")
    public ResponseEntity<APIRessponse> dispatch(@PathVariable Integer orderId) {
        log.info("API dispatch order. orderId={}", orderId);
        ShippingAssignment assignment = fulfillmentService.dispatchToShipper(orderId);
        return ResponseEntity.ok(new APIRessponse(200, "Đã bàn giao cho đơn vị vận chuyển", assignment));
    }

    /** Mock webhook 3PL; chỉ dùng demo, production cần xác thực chữ ký callback. */
    @PostMapping("/{orderId}/carrier/delivery-success")
    public ResponseEntity<APIRessponse> deliverySuccessful(
            @PathVariable Integer orderId,
            @RequestBody DeliverySuccessRequest request) {
        fulfillmentService.markDeliverySuccessful(orderId, request == null ? null : request.getTrackingNumber());
        return ResponseEntity.ok(new APIRessponse(200, "Shipper đã giao hàng thành công"));
    }

    @PostMapping("/{orderId}/confirm-received")
    public ResponseEntity<APIRessponse> confirmReceived(
            @PathVariable Integer orderId,
            @RequestHeader("X-Customer-Id") String customerId) {
        // Demo only: production lấy customerId từ authenticated principal, không tin header client tự gửi.
        fulfillmentService.confirmReceived(orderId, customerId);
        return ResponseEntity.ok(new APIRessponse(200, "Bạn đã xác nhận nhận hàng"));
    }

    public static class DeliverySuccessRequest {
        private String trackingNumber;
        public String getTrackingNumber() { return trackingNumber; }
        public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    }
}
