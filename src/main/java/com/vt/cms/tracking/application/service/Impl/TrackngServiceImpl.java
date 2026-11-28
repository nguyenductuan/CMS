package com.vt.cms.tracking.application.service.Impl;

import com.vt.cms.order.domain.entity.OrderTracking;
import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import com.vt.cms.order.domain.repository.OrderTrackingRepository;
import com.vt.cms.tracking.application.service.TrackingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackngServiceImpl implements TrackingService {
  private final OrderTrackingRepository orderTrackingRepository;

  @Override
  public void createtracking(List<Integer> orderIds, OrderTrackingStatus orderTrackingStatus) {
    List<OrderTracking> ordertracking =
        orderIds.stream().map(id -> buildtrackingstatus(id, orderTrackingStatus)).toList();
    orderTrackingRepository.createtracking(ordertracking);
  }

  public OrderTracking buildtrackingstatus(Integer id, OrderTrackingStatus orderTrackingStatus) {
    OrderTracking orderTracking = new OrderTracking();
    orderTracking.setBuyerMsg(orderTrackingStatus.getBuyerMsg());
    orderTracking.setOrderId(id);
    orderTracking.setOrderTrackingStatus(orderTrackingStatus);

    return orderTracking;
  }
}
