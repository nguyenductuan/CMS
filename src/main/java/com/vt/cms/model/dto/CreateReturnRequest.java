package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CreateReturnRequest {
    @NotBlank(message = "Lý do trả hàng không được để trống")
    @Size(max = 2000)
    private String reason;
    private List<String> images;
    private List<String> videos;
}
