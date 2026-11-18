package com.vt.cms.campain.application.dto.response;

import com.vt.cms.campain.domain.enums.CampaignAction;
import com.vt.cms.campain.domain.enums.CampainStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampainResponse {
  private Integer id;
  //  private String code;
  private String name;
  private String description;
  private List<CampaignProductGroupResponse> product;
  private List<CampaignAction> availableActions;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private CampainStatus status;
  private BigDecimal discount;
}
