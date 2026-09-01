package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private String paymanrtid;
    private Long OrderId;
    private String status;
    private  String trancactioncode;
    private BigDecimal amount;


}
