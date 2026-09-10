package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampainProduct {
private int campainid;
private long productId;
private LocalDateTime createddate;
    private LocalDateTime updateddate;
}
