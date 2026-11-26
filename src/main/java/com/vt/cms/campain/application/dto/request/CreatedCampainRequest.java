package com.vt.cms.campain.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CreatedCampainRequest {
  private String name;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String description;
  private BigDecimal discount;
  private Integer total_stock;
  /*TODO: Mục đích lưu stock trong bảng campain làm gì */
  private List<CampainProductRequest> products;
}
