package com.vt.cms.Shipping.application;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ShippingMethodDTO {
  private Long id;

  /** Tên gói vận chuyển, ví dụ: "Tiết kiệm", "Nhanh". */
  private String name;

  /** Mô tả thời gian nhận hàng, ví dụ: "Nhận trong: 11 th01 – 12 th01". */
  private String estimatedDelivery;

  /** Phí ship gốc trước khuyến mãi, dùng để hiển thị giá gạch ngang. */
  private BigDecimal originalFee;

  /** Phí ship cuối cùng sau khi áp mã freeship, có thể là 0đ. */
  private BigDecimal finalFee;

  /** Đã áp dụng freeship hay ưu đãi vận chuyển hay chưa. */
  private Boolean freeShippingApplied;
}
