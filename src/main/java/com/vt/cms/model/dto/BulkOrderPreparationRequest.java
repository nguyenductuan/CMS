package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BulkOrderPreparationRequest {
    @NotEmpty
    private List<Integer> orderIds;

    @NotBlank
    private String warehouseCode;

    private String note;
}