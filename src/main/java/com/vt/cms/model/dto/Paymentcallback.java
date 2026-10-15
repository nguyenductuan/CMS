package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Paymentcallback {
    private  String cusId;
    private BigDecimal amount;
    private  String partnerBankCode;
    private  String timeTransaction;
    private  String transactionCode;
    private  Integer partnerInternalId;

}
