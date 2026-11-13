package com.vt.cms.campain.domain.enity;

import com.vt.cms.campain.domain.enums.CampainStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Campain {
    private Integer campainid;
    private LocalDateTime createdAt;
    private String createdBy;
    private String isDeleted;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private CampainStatus status;
    private String updatedBy;
    private BigDecimal discount;
    private LocalDateTime endTime;
    private String name;
    private LocalDateTime startTime;
    private Integer totalStock;
    private Long productId;
    private String description;
//    private Integer salesCommission;
}
