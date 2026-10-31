package com.vt.cms.service;

import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.model.repository.OrderTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** Ghi nhận và truy vấn lịch sử tác động làm thay đổi trạng thái đơn hàng. */
@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final OrderTrackingRepository repository;

    /**
     * Mô tả trạng thái/sự kiện do đơn vị vận chuyển gửi về.
     * Dùng eventCode để phân biệt mã tracking với trạng thái nghiệp vụ của đơn hàng.
     */
    private static final Map<String, String> SHIPPING_ACTION_MAP = Map.ofEntries(
            Map.entry("103", "Đơn vị vận chuyển lấy hàng thành công"),
            Map.entry("104", "Đơn vị vận chuyển lấy hàng thành công"),
            Map.entry("101", "Đơn vị vận chuyển lấy hàng thất bại"),
            Map.entry("320", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("400", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("410", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("500", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("506", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("507", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("508", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("509", "Đơn vị vận chuyển đang giao hàng"),
            Map.entry("505", "Đơn hàng giao không thành công"),
            Map.entry("RETURN_PICKUP_SUCCESS", "Đơn vị vận chuyển lấy hàng hoàn thành công"),
            Map.entry("RETURN_PICKUP_FAILED", "Đơn vị vận chuyển lấy hàng hoàn thất bại"),
            Map.entry("RETURN_DELIVERY_SUCCESS", "Đơn vị vận chuyển giao hàng hoàn thành công"),
            Map.entry("RETURN_DELIVERY_FAILED", "Đơn vị vận chuyển giao hàng hoàn thất bại"),
            Map.entry("CARRIER_DELIVERY_SUCCESS", "Đơn vị vận chuyển giao hàng thành công")
    );


    /** Ghi một sự kiện lịch sử trong cùng transaction với thao tác thay đổi đơn hàng. */
    public void record(Integer orderId, String eventCode, String statusCode, String actor,
                       String actorRole, String title, String description) {
        OrderTracking event = new OrderTracking();
        event.setOrderId(orderId);
        event.setEventCode(eventCode);
        event.setStatusCode(statusCode);
        event.setActor(actor);
        event.setActorRole(actorRole);
        event.setTitle(title);
        event.setDescription(description);
        event.setCreatedAt(LocalDateTime.now());
        repository.insertordertracking(event);
    }

    /** Trả về lịch sử đơn hàng theo thời gian tăng dần. */
    public List<OrderTracking> getHistory(Integer orderId) {
        List<OrderTracking> history = repository.findByOrderId(orderId);
        for (OrderTracking event : history) {
            event.setStatusName(resolveStatusName(event));
        }
        return history;
    }

    /**
     * Lấy tên trạng thái hiển thị cho một dòng lịch sử.
     * Ưu tiên mô tả mã vận chuyển, sau đó đến tiêu đề sự kiện và trạng thái đơn hàng.
     */
    private String resolveStatusName(OrderTracking event) {
        if (event == null) {
            return null;
        }

        String eventCode = event.getEventCode();
        if (eventCode != null && !eventCode.isBlank()) {
            String shippingAction = SHIPPING_ACTION_MAP.get(eventCode.trim().toUpperCase());
            if (shippingAction != null) {
                return shippingAction;
            }
        }

        if (event.getTitle() != null && !event.getTitle().isBlank()) {
            return event.getTitle();
        }

        return event.getStatusCode();
    }
}
