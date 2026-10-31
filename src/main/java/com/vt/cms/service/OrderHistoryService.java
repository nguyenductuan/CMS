package com.vt.cms.service;

import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.model.repository.OrderTrackingRepository;
import com.vt.cms.model.enums.RoleAction;
import com.vt.cms.model.enums.OrderTrackingStatus;
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
    private static final Map<OrderTrackingStatus, String> SHIPPING_ACTION_MAP =
            Map.ofEntries(
                    Map.entry(OrderTrackingStatus.S103, "Đơn vị vận chuyển lấy hàng thành công"),
                    Map.entry(OrderTrackingStatus.S104, "Đơn vị vận chuyển lấy hàng thành công"),
                    Map.entry(OrderTrackingStatus.S101, "Đơn vị vận chuyển lấy hàng thất bại"),
                    Map.entry(OrderTrackingStatus.S320, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S400, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S410, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S500, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S506, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S507, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S508, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S509, "Đơn vị vận chuyển đang giao hàng"),
                    Map.entry(OrderTrackingStatus.S505, "Đơn hàng giao không thành công")
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
            event.setRoleAction(getRoleAction(toOrderTrackingStatus(event.getEventCode())));
        }
        return history;
    }

    /**
     * Xác định nhóm tác nhân theo mã trạng thái vận chuyển.
     * Các mã của đơn vị vận chuyển được phân loại là SHIPPING_UNIT;
     * các mã còn lại mặc định là SYSTEM, theo quy tắc nghiệp vụ hiện tại.
     */
    public RoleAction getRoleAction(OrderTrackingStatus status) {
        if (status == OrderTrackingStatus.S103
                || status == OrderTrackingStatus.S104
                || status == OrderTrackingStatus.S320
                || status == OrderTrackingStatus.S400
                || status == OrderTrackingStatus.S410
                || status == OrderTrackingStatus.S500
                || status == OrderTrackingStatus.S506
                || status == OrderTrackingStatus.S507
                || status == OrderTrackingStatus.S508
                || status == OrderTrackingStatus.S509) {
            return RoleAction.SHIPPING_UNIT;
        }
        return RoleAction.SYSTEM;
    }

    /**
     * Chuyển mã sự kiện dạng String trong database sang enum.
     * Các sự kiện nghiệp vụ không phải mã vận chuyển trả về null.
     */
    private OrderTrackingStatus toOrderTrackingStatus(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            return null;
        }
        try {
            return OrderTrackingStatus.valueOf("S" + eventCode.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
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
            String shippingAction = SHIPPING_ACTION_MAP.get(toOrderTrackingStatus(eventCode));
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
