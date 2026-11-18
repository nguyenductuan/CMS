package com.vt.cms.campain.domain.enity;

import com.vt.cms.campain.domain.enums.CampainStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Campain {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer campainId;

  private LocalDateTime createdAt;
  private String createdBy;
  private String isDeleted;
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  private CampainStatus status;

  private String updatedBy;
  private BigDecimal discount;
  private LocalDateTime endTime;
  private String name;
  private LocalDateTime startTime;
  private Integer totalStock;
  private Integer productId;
  private String description;
}
