package com.vt.cms.service;

import com.vt.cms.model.dto.WarehouseSelectionRequest;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.ShippingAssignment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderFulfillmentService {
    private final OrderRepository orderRepository;
    private final MockShippingProvider mockShippingProvider;
    private final OrderHistoryService orderHistoryService;

    @Transactional(rollbackFor = Exception.class)
    /**
     * Thực hiện xử lý nghiệp vụ select warehouse. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void selectWarehouse(Integer orderId, WarehouseSelectionRequest request, String actor, String actorRole) {
        validateOrderId(orderId);
        if (request == null || request.getWarehouseCode() == null || request.getWarehouseCode().isBlank()) {
            throw new IllegalArgumentException("Mã kho không được để trống");
        }
        Order order = requireOrder(orderId);
        requireStatus(order, OrderStatus.PAID);

        int rows = orderRepository.selectWarehouse(orderId, request.getWarehouseCode().trim(),
                LocalDateTime.now(), actor);
        if (rows != 1) {
            log.warn("Warehouse selection rejected due to concurrent status change. orderId={}", orderId);
            throw new IllegalStateException("Không thể chọn kho: trạng thái đơn hàng đã thay đổi");
        }
        orderHistoryService.record(orderId, "008", "PREPARING", actor, actorRole, "Seller xác nhận đã chuẩn bị hàng thành công", "Kho được chọn: " + request.getWarehouseCode().trim());
        log.info("Warehouse selected. orderId={}, warehouseCode={}", orderId, request.getWarehouseCode().trim());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * Thực hiện xử lý nghiệp vụ dispatch to shipper. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ShippingAssignment dispatchToShipper(Integer orderId) {
        validateOrderId(orderId);
        Order order = requireOrder(orderId);
        requireStatus(order, OrderStatus.PREPARING);
        if (order.getWarehouseCode() == null || order.getWarehouseCode().isBlank()) {
            throw new IllegalStateException("Đơn hàng chưa được chọn kho");
        }

        ShippingAssignment assignment = mockShippingProvider.assignShipper(orderId, order.getWarehouseCode());
        int rows = orderRepository.assignShipper(orderId, OrderStatus.PREPARING.name(),
                OrderStatus.SHIPPING.name(), assignment.getProvider(), assignment.getTrackingNumber(),
                assignment.getShipperCode(), assignment.getShipperName(), assignment.getShipperPhone(),
                LocalDateTime.now(), "MOCK-3PL");
        if (rows != 1) {
            log.warn("Dispatch rejected due to concurrent status change. orderId={}", orderId);
            throw new IllegalStateException("Không thể bàn giao vận chuyển: trạng thái đơn hàng đã thay đổi");
        }
        orderHistoryService.record(orderId, "SHIPPER_ASSIGNED", "SHIPPING", "SYSTEM", "SYSTEM",
                "Phân công giao hàng cho shipper thành công",
                "Đơn vị vận chuyển: " + assignment.getProvider() + "; shipper: " + assignment.getShipperName()
                        + "; mã vận đơn: " + assignment.getTrackingNumber());
        log.info("Order dispatched. orderId={}, provider={}, trackingNumber={}, shipperCode={}",
                orderId, assignment.getProvider(), assignment.getTrackingNumber(), assignment.getShipperCode());
        return assignment;
    }

    /** Mô phỏng webhook từ hãng vận chuyển. Production phải xác thực chữ ký webhook. */
    @Transactional(rollbackFor = Exception.class)
    /**
     * Thực hiện xử lý nghiệp vụ mark delivery successful. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void markDeliverySuccessful(Integer orderId, String trackingNumber) {
        validateOrderId(orderId);
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Mã vận đơn không được để trống");
        }
        int rows = orderRepository.markDeliverySuccessful(orderId, trackingNumber.trim(),
                OrderStatus.SHIPPING.name(), OrderStatus.DELIVERED.name(), LocalDateTime.now(), "MOCK-3PL");
        if (rows != 1) {
            log.warn("Delivery callback rejected. orderId={}, trackingNumber={}", orderId, trackingNumber);
            throw new IllegalStateException("Không tìm thấy vận đơn đang giao hoặc trạng thái không hợp lệ");
        }
        orderRepository.updateShippingStatusCode(orderId, "CARRIER_DELIVERY_SUCCESS", LocalDateTime.now(), "MOCK-3PL");
        orderHistoryService.record(orderId, "CARRIER_DELIVERY_SUCCESS", "DELIVERED", "MOCK-3PL", "CARRIER", "Giao hàng thành công theo callback giao hàng", "Mã vận đơn: " + trackingNumber);
        log.info("Carrier reports delivery successful. orderId={}, trackingNumber={}", orderId, trackingNumber);
    }

    /**
     * Ghi nhận trạng thái vận chuyển do đơn vị vận chuyển gửi về.
     * Hỗ trợ mã 101, 103, 104, 320, 410, 400, 500, 506, 507, 508, 509 và 505.
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordCarrierTrackingEvent(Integer orderId, String code, String description) {
        validateOrderId(orderId);
        java.util.Map<String, String> titles = java.util.Map.ofEntries(
                java.util.Map.entry("101", "Đơn vị vận chuyển lấy hàng thất bại"),
                java.util.Map.entry("103", "Đơn vị vận chuyển lấy hàng thành công"),
                java.util.Map.entry("104", "Đơn vị vận chuyển đã nhận hàng"),
                java.util.Map.entry("200", "Đơn hàng đang giao"),
                java.util.Map.entry("320", "Đơn hàng đang vận chuyển"),
                java.util.Map.entry("410", "Đơn hàng đang giao"),
                java.util.Map.entry("400", "Đơn hàng đang giao"),
                java.util.Map.entry("500", "Đơn hàng đang vận chuyển/đang giao"),
                java.util.Map.entry("506", "Đơn hàng đang giao"),
                java.util.Map.entry("507", "Đơn hàng đang giao"),
                java.util.Map.entry("508", "Đơn hàng đang giao"),
                java.util.Map.entry("509", "Đơn hàng đang giao"),
                java.util.Map.entry("505", "Đơn hàng giao không thành công"),
                java.util.Map.entry("RETURN_PICKUP_SUCCESS", "Đơn vị vận chuyển lấy hàng hoàn thành công"),
                java.util.Map.entry("RETURN_PICKUP_FAILED", "Đơn vị vận chuyển lấy hàng hoàn thất bại"),
                java.util.Map.entry("RETURN_DELIVERY_SUCCESS", "Đơn vị vận chuyển giao hàng hoàn thành công"),
                java.util.Map.entry("RETURN_DELIVERY_FAILED", "Đơn vị vận chuyển giao hàng hoàn thất bại"));
        String title = titles.get(code);
        if (title == null) throw new IllegalArgumentException("Mã trạng thái vận chuyển không được hỗ trợ");
        int updated = orderRepository.updateShippingStatusCode(orderId, code, LocalDateTime.now(), "CARRIER");
        if (updated != 1) throw new IllegalStateException("Không thể cập nhật mã trạng thái vận chuyển");
        orderHistoryService.record(orderId, code, code, "CARRIER", "CARRIER", title, description);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * Xác nhận nghiệp vụ confirm received. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void confirmReceived(Integer orderId, String customerId) {
        validateOrderId(orderId);
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Không xác định được người nhận hàng");
        }
        int rows = orderRepository.confirmReceived(orderId, OrderStatus.DELIVERED.name(),
                OrderStatus.COMPLETED.name(), LocalDateTime.now(), customerId);
        if (rows != 1) {
            log.warn("Receive confirmation rejected. orderId={}, customerId={}", orderId, customerId);
            throw new IllegalStateException("Chỉ có thể xác nhận nhận hàng khi shipper đã giao thành công");
        }
        orderHistoryService.record(orderId, "501", "COMPLETED", customerId, "BUYER", "Buyer nhấn Đã nhận hàng", "Buyer xác nhận đã nhận được hàng");
        log.info("Customer confirmed order received; order completed. orderId={}, customerId={}", orderId, customerId);
    }

    /**
     * Kiểm tra điều kiện require order. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private Order requireOrder(Integer orderId) {
        Order order = orderRepository.getorderbyId(orderId);
        if (order == null) {
            log.warn("Order not found. orderId={}", orderId);
            throw new IllegalArgumentException("Không tìm thấy đơn hàng: " + orderId);
        }
        return order;
    }

    /**
     * Kiểm tra điều kiện require status. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void requireStatus(Order order, OrderStatus expected) {
        if (order.getOrder_status() != expected) {
            log.warn("Invalid order transition. orderId={}, currentStatus={}, expectedStatus={}",
                    order.getId(), order.getOrder_status(), expected);
            throw new IllegalStateException("Trạng thái đơn hàng phải là " + expected.getDescription());
        }
    }

    /**
     * Kiểm tra tính hợp lệ validate order id. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateOrderId(Integer orderId) {
        if (orderId == null || orderId <= 0) throw new IllegalArgumentException("ID đơn hàng không hợp lệ");
    }
}
