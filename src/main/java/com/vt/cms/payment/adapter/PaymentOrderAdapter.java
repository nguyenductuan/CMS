package com.vt.cms.payment.adapter;

import com.vt.cms.order.application.dto.response.OrderResponse;
import com.vt.cms.order.application.service.BuyerOrderService;
import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import com.vt.cms.payment.application.service.port.OrderPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderAdapter implements OrderPort {
  final BuyerOrderService buyerOrderService;

  public PaymentOrderAdapter(BuyerOrderService buyerOrderService) {
    this.buyerOrderService = buyerOrderService;
  }

  @Override
  public List<OrderResponse> getOrderByIds(List<Integer> orderIds) {
    return buyerOrderService.getOrderByIds(orderIds);
  }

  @Override
  public void uploadOrder(
      List<Integer> orderId, OrderStatus orderStatus, OrderSubStatus orderSubStatus) {
    buyerOrderService.uploadOrderStatus(orderId, orderStatus, orderSubStatus);
  }
}
