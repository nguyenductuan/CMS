package com.vt.cms.order.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PreparedOrderRequest {
    @NotEmpty
    private List<Long> orderId;
    private String note;
}
