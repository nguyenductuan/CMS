package com.vt.cms.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreatedCampainRequest {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private int discount;
    private Integer salesCommission;
    private List<CampainProductRequest> products;
}
