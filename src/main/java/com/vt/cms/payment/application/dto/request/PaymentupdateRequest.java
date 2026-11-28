package com.vt.cms.payment.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentupdateRequest {
  private Integer cusId;
  private BigDecimal amount;
  private String partnerBankCode;
  private LocalDateTime timeTransaction;
  private String transactionCode;
  private String partnerInternalId;
}
