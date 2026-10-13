package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreatedCampainRequest {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private BigDecimal discount;
    private Integer total_stock;
    private Integer salesCommission;
    private List<CampainProductRequest> products;
}
