package com.vt.cms.model.resp;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CampainResponse {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private String products;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private BigDecimal discount;
}
