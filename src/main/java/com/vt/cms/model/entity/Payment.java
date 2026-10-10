package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private Integer id;
    private Integer orderId;
    private String status;
    private String transactionCode;
    private BigDecimal amount;
}
