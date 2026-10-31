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
    /**
     * Thực hiện xử lý nghiệp vụ select warehouse. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> selectWarehouse(
            @PathVariable Integer orderId,
            @Valid @RequestBody WarehouseSelectionRequest request) {
        log.info("API select warehouse. orderId={}", orderId);
        fulfillmentService.selectWarehouse(orderId, request);
        return ResponseEntity.ok(new APIRessponse(200, "Đã chọn kho, đơn hàng đang chuẩn bị"));
    }

    @PostMapping("/{orderId}/dispatch")
    /**
     * Thực hiện xử lý nghiệp vụ dispatch. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> dispatch(@PathVariable Integer orderId) {
        log.info("API dispatch order. orderId={}", orderId);
        ShippingAssignment assignment = fulfillmentService.dispatchToShipper(orderId);
        return ResponseEntity.ok(new APIRessponse(200, "Đã bàn giao cho đơn vị vận chuyển", assignment));
    }

    /** Mock webhook 3PL; chỉ dùng demo, production cần xác thực chữ ký callback. */
    @PostMapping("/{orderId}/carrier/delivery-success")
    /**
     * Thực hiện xử lý nghiệp vụ delivery successful. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> deliverySuccessful(
            @PathVariable Integer orderId,
            @RequestBody DeliverySuccessRequest request) {
        fulfillmentService.markDeliverySuccessful(orderId, request == null ? null : request.getTrackingNumber());
        return ResponseEntity.ok(new APIRessponse(200, "Shipper đã giao hàng thành công"));
    }

    /** API nhận trạng thái tracking do đơn vị vận chuyển callback. */
    @PostMapping("/{orderId}/carrier/tracking-event")
    public ResponseEntity<APIRessponse> carrierTrackingEvent(
            @PathVariable Integer orderId, @RequestBody CarrierTrackingEventRequest request) {
        fulfillmentService.recordCarrierTrackingEvent(orderId, request.getCode(), request.getDescription());
        return ResponseEntity.ok(new APIRessponse(200, "Đã ghi nhận trạng thái vận chuyển"));
    }

    @PostMapping("/{orderId}/confirm-received")
    /**
     * Xác nhận nghiệp vụ confirm received. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> confirmReceived(
            @PathVariable Integer orderId,
            @RequestHeader("X-Customer-Id") String customerId) {
        // Demo only: production lấy customerId từ authenticated principal, không tin header client tự gửi.
        fulfillmentService.confirmReceived(orderId, customerId);
        return ResponseEntity.ok(new APIRessponse(200, "Bạn đã xác nhận nhận hàng"));
    }

    public static class CarrierTrackingEventRequest {
        private String code;
        private String description;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class DeliverySuccessRequest {
        private String trackingNumber;
        /**
         * Lấy dữ liệu get tracking number. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
         */
        public String getTrackingNumber() { return trackingNumber; }
        /**
         * Thiết lập hoặc cập nhật set tracking number. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
         */
        public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    }
}
