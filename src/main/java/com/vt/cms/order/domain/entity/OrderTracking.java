package com.vt.cms.order.domain.entity;

import com.vt.cms.order.domain.enums.EvtpOrderStatus;
import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import lombok.Data;

@Data
public class OrderTracking {
  private Integer orderId;
  private String orderEvtpId;
  private OrderTrackingStatus orderTrackingStatus;
  private EvtpOrderStatus evtpOrderStatus;
  private String buyerMsg;
  private String images;
}
