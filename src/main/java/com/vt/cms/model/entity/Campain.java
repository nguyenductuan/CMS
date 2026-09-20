package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Campain {
    private Integer campainid;
    private LocalDateTime createdAt;
    private String createdBy;
    private String isDeleted;
    private LocalDateTime updatedAt;
    private String status;
    private String updatedBy;
    private Long version;
    private Integer discount;
    private LocalDateTime endTime;
    private String name;
    private Integer quantity;
    private LocalDateTime startTime;
    private Integer totalStock;
    private Long productId;
    private String description;
    private Integer salesCommission;
}
