package com.vt.cms.model.entity;

import com.vt.cms.model.enums.TrackingStatus;
import lombok.Data;

@Data
public class OrderTracking {
    private Integer id;
    private Integer orderId;
    private String status;
    private String title;
    private String description;

}
