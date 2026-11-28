package com.vt.cms.order.domain.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EvtpOrderStatus {
  S101("101", "Viettel Post tự lấy hàng", true),
  S102("102", "Lấy hàng thất bại", false),
  S103("103", "Điều phối bưu cục lấy hàng", false),
  S104("104", "Lấy hàng điều phối bưu tá", false),
  S107("107", "Đối tác yêu cầu hủy", true),
  S200("200", "Lấy hàng thành công", false),
  S201("201", "Viettel Post hủy đơn hàng", true),
  S202("202", "Sửa phiếu gửi", false),
  S300("300", "Khai thác đi", false),
  S400("400", "Khai thác đến", false),
  S410("410", "Nhận tải/kiện chuyển tiếp", false),
  S500("500", "Giao bưu tá đi phát", false),
  S501("501", "Thành công – Phát thành công", true),
  S503("503", "Tiêu hủy – Theo yêu cầu khách hàng", true),
  S504("504", "Thành công – Chuyển trả người gửi (Đơn hàng hoàn)", true),
  S505("505", "Yêu cầu chuyển hoàn", false),
  S506("506", "Phát thất bại - Khách hàng nghỉ, không có nhà/ Không nghe máy/ Hẹn giao lại", false),
  S507("507", "Phát thất bại - Khách hàng đến bưu cục nhận", false),
  S508("508", "Đơn vị yêu cầu phát tiếp", false),
  S509("509", "Chuyển tiếp bưu cục khác", false),
  S515("515", "Bưu cục phát duyệt hoàn", false),
  S550("550", "Khách hàng yêu cầu phát tiếp", false),
  ;

  private final String code;
  private final String description;
  private final boolean isFinalStatus;

  public static Optional<EvtpOrderStatus> fromCode(String code) {
    return Arrays.stream(EvtpOrderStatus.values())
        .filter(status -> status.getCode().equals(code))
        .findFirst();
  }
}
