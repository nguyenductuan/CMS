package com.vt.cms.service;

import com.vt.cms.model.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Tự động chuyển đơn DELIVERED sang COMPLETED sau 5 ngày,
 * nếu khách hàng chưa chủ động xác nhận nhận hàng.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveredOrderCompletionJob {
    private static final int AUTO_COMPLETE_AFTER_DAYS = 5;

    private final OrderRepository orderRepository;

    /**
     * Chạy mỗi giờ vào phút thứ 0 theo múi giờ Việt Nam.
     * SQL chỉ cập nhật đơn vẫn DELIVERED và đã có thời điểm giao thành công.
     */
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Ho_Chi_Minh")
    @Transactional(rollbackFor = Exception.class)
    public void completeDeliveredOrders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusDays(AUTO_COMPLETE_AFTER_DAYS);

        int updatedCount = orderRepository.completeDeliveredOrdersOlderThan(
                cutoff, now, "SYSTEM_JOB");

        if (updatedCount > 0) {
            log.info("Auto-completed delivered orders after {} days. count={}, cutoff={}",
                    AUTO_COMPLETE_AFTER_DAYS, updatedCount, cutoff);
        } else {
            log.debug("No delivered orders eligible for auto-completion. cutoff={}", cutoff);
        }
    }
}
