package com.vt.cms.order.application.dto.response;

import com.vt.cms.payment.domain.enums.PaymentMethod;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class OrderCreateResponse {
  private List<Integer> orderId;
  private PaymentMethod payment_method;
  private BigDecimal total_payment;
}
