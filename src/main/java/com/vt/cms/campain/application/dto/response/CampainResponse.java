package com.vt.cms.campain.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

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
