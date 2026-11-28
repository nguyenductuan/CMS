package com.vt.cms.payment.application.service.port;

import com.vt.cms.order.application.dto.response.OrderResponse;
import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import java.util.List;

public interface OrderPort {
  List<OrderResponse> getOrderByIds(List<Integer> orderIds);

  void uploadOrder(List<Integer> orderId, OrderStatus orderStatus, OrderSubStatus orderSubStatus);
}
