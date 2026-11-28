package com.vt.cms.payment.adapter;

import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import com.vt.cms.payment.application.service.port.OrderTrackingPort;
import com.vt.cms.tracking.application.service.TrackingService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentTrackingAdapter implements OrderTrackingPort {

  final TrackingService trackingService;

  public PaymentTrackingAdapter(TrackingService trackingService) {
    this.trackingService = trackingService;
  }

  @Override
  public void creatTracking(List<Integer> orderIds, OrderTrackingStatus orderTrackingStatus) {
    trackingService.createtracking(orderIds, orderTrackingStatus);
  }
}
