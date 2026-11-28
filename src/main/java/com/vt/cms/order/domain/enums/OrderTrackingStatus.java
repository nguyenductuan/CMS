package com.vt.cms.order.domain.enums;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Mã sự kiện tracking nhận từ đơn vị vận chuyển. Các mã được đặt tên theo đúng mã tracking để thuận
 * tiện đối chiếu callback.
 */
@Getter
@AllArgsConstructor
public enum OrderTrackingStatus {
  S001(
      "001",
      OrderStatus.WAIT_PAYMENT,
      "Khách hàng tạo đơn trên hệ thống F2C, đơn hàng đang chờ thanh toán"),
  S002("002", OrderStatus.WAIT_PAYMENT, "Đơn sinh mã thanh toán thất bại, Đang chờ thanh toán lại"),
  S003(
      "003",
      OrderStatus.CANCELLED,
      "Đơn hàng quá hạn thanh toán, đã được hủy bởi F2C sau khi hết hạn thanh toán"),
  S005("005", OrderStatus.WAIT_SHIPPING, "Nhà bán đang chuẩn bị hàng"),
  S006("006", OrderStatus.CANCELLED, "Đơn hàng được hủy bởi Seller"),
  S007("007", OrderStatus.CANCELLED, "Đơn hàng được hủy bởi khách hàng"),
  S008(
      "008",
      OrderStatus.WAIT_SHIPPING,
      "Nhà bán chuẩn bị hàng thành công, đang chờ đơn vị vận chuyển"),
  S101("101", OrderStatus.CANCELLED, "Đơn hàng được hủy bởi VTP"),
  S102("102", OrderStatus.WAIT_SHIPPING, "Lấy hàng thất bại"),
  S103("103", OrderStatus.WAIT_SHIPPING, "Đơn hàng giao cho bưu cục"),
  S104("104", OrderStatus.WAIT_SHIPPING, "Đơn hàng đã giao cho bưu tá"),
  S200("200", OrderStatus.SHIPPING, "Đơn hàng đã được ViettelPost nhập doanh thu"),
  S201("201", OrderStatus.CANCELLED, "Đơn hàng được hủy bởi VTP"),
  S202("202", OrderStatus.SHIPPING, "Đơn hàng đã đến Việt Nam - chưa thông quan"),
  S300("300", OrderStatus.SHIPPING, "Đơn hàng đã đến Việt Nam"),
  S320("320", OrderStatus.SHIPPING, "Đang vận chuyển - Đơn hàng đang trên đường tới bưu cục"),
  S410("410", OrderStatus.SHIPPING, "Đang vận chuyển - Đơn hàng đã đến bưu cục"),
  S400("400", OrderStatus.SHIPPING, "Đang vận chuyển - Đơn hàng đã đến bưu cục"),
  S500("500", OrderStatus.SHIPPING, "Đang giao hàng - Đơn hàng đang trên đường giao tới bạn"),
  S501("501", OrderStatus.SHIPPING, "Giao hàng thành công"),
  // S503("503", OrderStatus.RETURN_FAILED_DESTROYED, "ĐVVC giao hàng thất bại - Tiêu hủy"),
  // S505("505", OrderStatus.RETURNED, "Hoàn hàng"),
  // S506("506", OrderStatus.RETURN_FAILED_STORED, "Giao thất bại - Lưu kho"),
  S507("507", OrderStatus.SHIPPING, "Đang giao hàng"),
  S508("508", OrderStatus.SHIPPING, "Đang giao hàng"),
  S509("509", OrderStatus.SHIPPING, "Đang giao hàng"),
  // S515("515", OrderStatus.RETURNED, "Khách hàng/Bưu cục gốc đồng ý duyệt hoàn"),
  S516("516", OrderStatus.SHIPPING, "Chờ đền bù"),
  S517("517", OrderStatus.SHIPPING, "Đã đền bù"),
  // S502("502", OrderStatus.RETURNED, "Bưu gửi được chuyển hoàn về bưu cục gốc"),
  // S504("504", OrderStatus.RETURNED, "Hoàn thành công"),
  S510("510", OrderStatus.COMPLETED, "Người mua đã nhận được hàng"),

  // R001("R001", OrderStatus.RETURN_PENDING, "Người mua tạo yêu cầu Trả hàng/Hoàn tiền"),
  // R002("R002", OrderStatus.RETURN_WAIT_SHIPPING, "Seller phê duyệt yêu cầu Trả hàng/Hoàn tiền"),
  R003("R003", OrderStatus.DELIVERED, "Seller từ chối yêu cầu Trả hàng/Hoàn tiền"),
  // R012("R012", OrderStatus.RETURN_WAIT_SHIPPING, "Admin phê duyệt yêu cầu Trả hàng/Hoàn tiền"),
  R013("R013", OrderStatus.DELIVERED, "Admin từ chối yêu cầu Trả hàng/Hoàn tiền"),
  R004("R004", OrderStatus.DELIVERED, "Người mua huỷ yêu cầu Trả hàng/Hoàn tiền"),
  //  R005("R200", OrderStatus.RETURN_SHIPPING, "Đơn vị vận chuyển lấy hàng hoàn thành công"),
  //  R006("R102", OrderStatus.RETURN_WAIT_SHIPPING, "Đơn vị vận chuyển lấy hàng hoàn thất bại"),
  //  R007("R501", OrderStatus.RETURN_DELIVERED, "Đơn vị vận chuyển giao hàng hoàn thành công"),
  R008("R008", OrderStatus.DELIVERED, "Sàn đã hủy yêu cầu Trả hàng/Hoàn tiền"),
  ;

  public static final Set<OrderTrackingStatus> CANCELLABLE_STATUSES =
      EnumSet.of(
          OrderTrackingStatus.S103,
          OrderTrackingStatus.S104,
          OrderTrackingStatus.S001,
          OrderTrackingStatus.S002,
          OrderTrackingStatus.S005,
          OrderTrackingStatus.S008);
  private final String value;
  private final OrderStatus orderStatus;
  private final String buyerMsg;

  public static Optional<OrderTrackingStatus> fromEvtpStatus(String evtpStatus) {
    return Arrays.stream(OrderTrackingStatus.values())
        .filter(status -> status.getValue().equals(evtpStatus))
        .findFirst();
  }
}
