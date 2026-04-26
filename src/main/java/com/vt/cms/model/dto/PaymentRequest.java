package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private int orderId;
    private String result;
}
