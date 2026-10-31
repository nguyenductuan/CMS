package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnReviewRequest {
    @NotNull
    private Boolean approved;
    private String note;
}
