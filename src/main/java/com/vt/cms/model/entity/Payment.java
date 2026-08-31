package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private String paymanrtid;
    private String orderid;
    private String status;
    private  String trancactionid;
    private BigDecimal amount;
}
