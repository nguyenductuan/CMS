package com.vt.cms.tracking.application.service;

import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import java.util.List;

public interface TrackingService {
  void createtracking(List<Integer> orderIds, OrderTrackingStatus orderTrackingStatus);
}
