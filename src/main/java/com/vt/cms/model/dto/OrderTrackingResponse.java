package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderTrackingResponse {
    private Long orderId;
    private String trackingCode;
    private List<Trackingstep> steps;

}
