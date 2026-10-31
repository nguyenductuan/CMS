package com.vt.cms.model.entity;

import com.vt.cms.model.enums.TrackingStatus;
import com.vt.cms.model.enums.RoleAction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class OrderTracking {
    private Integer id;
    private Integer orderId;

    private String statusCode;
    private String statusName;
    private String title;
    private String description;
    private String actor;
    private String actorRole;
    private RoleAction roleAction;
    private String eventCode;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private java.time.LocalDateTime createdAt;

}
