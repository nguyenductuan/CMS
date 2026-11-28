package com.vt.cms.payment.domain.entity;

import com.vt.cms.payment.domain.enums.PaymentMethod;
import com.vt.cms.payment.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Payment {
  private Integer id;
  private Integer orderId;
  private PaymentStatus status;
  private String transactionCode;
  private BigDecimal totalAmount;
  private PaymentMethod paymentMethod;
  private LocalDateTime expiredTime;
  private String genQr;
  private LocalDateTime paidAt;
}
