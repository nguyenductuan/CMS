package com.vt.cms.model.entity;

import com.vt.cms.model.enums.TrackingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderTracking {
    private Integer id;
    private Integer orderId;

    private String statusCode;
    private String statusName;
    private String title;
    private String description;

}
