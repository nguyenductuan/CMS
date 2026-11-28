package com.vt.cms.payment.application.service.port;

import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import java.util.List;

public interface OrderTrackingPort {
  void creatTracking(List<Integer> orderIds, OrderTrackingStatus orderTrackingStatus);
}
