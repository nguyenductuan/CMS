package com.vt.cms.payment.application.dto.request;

import com.vt.cms.payment.domain.enums.PaymentMethod;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class PaymentRequest {

  private List<Integer> orderIds;

  private PaymentMethod paymentMethod;

  private BigDecimal totalPayment;
}
