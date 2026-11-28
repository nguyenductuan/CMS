package com.vt.cms.payment.domain.entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Paymentorder {
  private Integer id;
  private Integer orderId;
  private Integer paymentId;
  private BigDecimal amount;
}
