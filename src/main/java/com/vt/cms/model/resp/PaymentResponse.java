package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String payment_id;
    private String qr_code;
    private BigDecimal amount;
    private String transaction_code;
    private LocalDateTime created_qr_time;
    private LocalDateTime expired_qr_time;

}
