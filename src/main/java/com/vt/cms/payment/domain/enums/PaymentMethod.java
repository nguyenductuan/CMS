package com.vt.cms.payment.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
  QR("Thanh toán trả trước"),
  BANK_TRANSFER("CHuyển khoản ngân hàng");

  private final String description;

  PaymentMethod(String description) {
    this.description = description;
  }
}
