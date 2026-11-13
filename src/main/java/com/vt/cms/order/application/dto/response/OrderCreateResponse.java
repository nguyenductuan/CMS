package com.vt.cms.order.application.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateResponse {
    private Integer orderId;
    private  String payment_method;
    private BigDecimal total_payment;


}
