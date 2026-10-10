package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentRequest {
   private Integer orderIds;
   private String paymentMethod;
    private String paymentProvider;
    private BigDecimal totalPayment;
}
