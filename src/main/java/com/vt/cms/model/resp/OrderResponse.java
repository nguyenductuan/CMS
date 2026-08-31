package com.vt.cms.model.resp;


import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {

    private String message;
    private DataResponse data;
    @Data
    public static class DataResponse {
        private  List<String> orderId;
        private String paymentmethod;
        private BigDecimal totalpayment;
    }
}
