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

    @Transactional(rollbackFor = Exception.class)
    /**
     * Thực hiện xử lý nghiệp vụ select warehouse. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void selectWarehouse(Integer orderId, WarehouseSelectionRequest request) {
        validateOrderId(orderId);
        if (request == null || request.getWarehouseCode() == null || request.getWarehouseCode().isBlank()) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Mã kho không được để trống");
        }
        Order order = requireOrder(orderId);
        requireStatus(order, OrderStatus.PAID);

        int rows = orderRepository.selectWarehouse(orderId, request.getWarehouseCode().trim(),
                LocalDateTime.now(), "SYSTEM");
        if (rows != 1) {
            log.warn("Warehouse selection rejected due to concurrent status change. orderId={}", orderId);
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không thể chọn kho: trạng thái đơn hàng đã thay đổi");
        }
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
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Đơn hàng chưa được chọn kho");
        }

        ShippingAssignment assignment = mockShippingProvider.assignShipper(orderId, order.getWarehouseCode());
        int rows = orderRepository.assignShipper(orderId, OrderStatus.PREPARING.name(),
                OrderStatus.SHIPPING.name(), assignment.getProvider(), assignment.getTrackingNumber(),
                assignment.getShipperCode(), assignment.getShipperName(), assignment.getShipperPhone(),
                LocalDateTime.now(), "MOCK-3PL");
        if (rows != 1) {
            log.warn("Dispatch rejected due to concurrent status change. orderId={}", orderId);
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không thể bàn giao vận chuyển: trạng thái đơn hàng đã thay đổi");
        }
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
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Mã vận đơn không được để trống");
        }
        int rows = orderRepository.markDeliverySuccessful(orderId, trackingNumber.trim(),
                OrderStatus.SHIPPING.name(), OrderStatus.DELIVERED.name(), LocalDateTime.now(), "MOCK-3PL");
        if (rows != 1) {
            log.warn("Delivery callback rejected. orderId={}, trackingNumber={}", orderId, trackingNumber);
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không tìm thấy vận đơn đang giao hoặc trạng thái không hợp lệ");
        }
        log.info("Carrier reports delivery successful. orderId={}, trackingNumber={}", orderId, trackingNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * Xác nhận nghiệp vụ confirm received. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void confirmReceived(Integer orderId, String customerId) {
        validateOrderId(orderId);
        if (customerId == null || customerId.isBlank()) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không xác định được người nhận hàng");
        }
        int rows = orderRepository.confirmReceived(orderId, OrderStatus.DELIVERED.name(),
                OrderStatus.COMPLETED.name(), LocalDateTime.now(), customerId);
        if (rows != 1) {
            log.warn("Receive confirmation rejected. orderId={}, customerId={}", orderId, customerId);
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Chỉ có thể xác nhận nhận hàng khi shipper đã giao thành công");
        }
        log.info("Customer confirmed order received; order completed. orderId={}, customerId={}", orderId, customerId);
    }

    /**
     * Kiểm tra điều kiện require order. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private Order requireOrder(Integer orderId) {
        Order order = orderRepository.getorderbyId(orderId);
        if (order == null) {
            log.warn("Order not found. orderId={}", orderId);
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
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
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
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
