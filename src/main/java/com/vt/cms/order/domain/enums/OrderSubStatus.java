package com.vt.cms.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderSubStatus {
  PREPARING("Chờ giao hàng"),
  ;
  private final String value;
}
