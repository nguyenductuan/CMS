package com.vt.cms.order.domain.entity;

import lombok.Data;

@Data
public class OrderActionLog {
  private Integer id;
  private Integer order_id;
  private String action;
  private String role_action;
  private String description;
}
