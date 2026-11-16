package com.vt.cms.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CreateReviewRequest {
    @NotNull
    private Integer orderId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer skuId;
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;
    @Size(max = 2000)
    private String content;
    private List<String> images;
    private List<String> videos;
}
