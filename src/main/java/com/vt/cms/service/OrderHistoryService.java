package com.vt.cms.service;

import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.model.repository.OrderTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/** Ghi nhận và truy vấn lịch sử tác động làm thay đổi trạng thái đơn hàng. */
@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final OrderTrackingRepository repository;

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
        return repository.findByOrderId(orderId);
    }
}
