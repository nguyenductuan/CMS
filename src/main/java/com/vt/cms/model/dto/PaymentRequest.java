package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRequest {
    private String transactionCode;
    private LocalDateTime timeTransaction;
    private BigDecimal amount;
    private String partnerBankCode;
    private String result;
}
