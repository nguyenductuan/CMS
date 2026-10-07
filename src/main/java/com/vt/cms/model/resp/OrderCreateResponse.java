package com.vt.cms.model.resp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateResponse {
    private Integer orderId;
    private  String payment_method;
    private BigDecimal total_payment;


}
