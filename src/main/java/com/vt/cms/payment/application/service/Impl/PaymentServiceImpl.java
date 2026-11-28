package com.vt.cms.payment.application.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.order.application.dto.response.OrderResponse;
import com.vt.cms.order.domain.entity.OrderActionLog;
import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import com.vt.cms.order.domain.enums.OrderTrackingStatus;
import com.vt.cms.order.domain.repository.OrderActionLogRepository;
import com.vt.cms.payment.application.dto.request.PaymentRequest;
import com.vt.cms.payment.application.dto.request.PaymentupdateRequest;
import com.vt.cms.payment.application.service.PaymentService;
import com.vt.cms.payment.application.service.port.OrderPort;
import com.vt.cms.payment.application.service.port.OrderTrackingPort;
import com.vt.cms.payment.domain.entity.Payment;
import com.vt.cms.payment.domain.entity.Paymentorder;
import com.vt.cms.payment.domain.enums.PaymentMethod;
import com.vt.cms.payment.domain.enums.PaymentStatus;
import com.vt.cms.payment.domain.repository.PaymentOrderRepostitory;
import com.vt.cms.payment.domain.repository.PaymentRepostitory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
  final PaymentRepostitory paymentRepostitory;
  final PaymentOrderRepostitory paymentOrderRepostitory;
  final OrderPort orderPort;
  final OrderTrackingPort orderTrackingPort;
  final OrderActionLogRepository orderActionLogRepository;

  public PaymentServiceImpl(
      PaymentRepostitory paymentRepostitory,
      PaymentOrderRepostitory paymentOrderRepostitory,
      OrderPort orderPort,
      OrderTrackingPort orderTrackingPort,
      OrderActionLogRepository orderActionLogRepository) {
    this.paymentRepostitory = paymentRepostitory;
    this.paymentOrderRepostitory = paymentOrderRepostitory;
    this.orderPort = orderPort;
    this.orderTrackingPort = orderTrackingPort;
    this.orderActionLogRepository = orderActionLogRepository;
  }

  @Override
  public void creatpayment(PaymentRequest request) {
    // Lấy danh sách thông tin danh sách orderds
    List<Integer> orderIds = request.getOrderIds();
    // Lấy sanh sách order theo orderids
    List<OrderResponse> orderlist = orderPort.getOrderByIds(orderIds);
    // valdate trạng thái danh sách order
    // valdate tổng tiền của danh sách order
    // Check mếu đơn hàng có giá trị =0 thì hiển thị đơn chờ giao hàng luôn và ghi vào lịch sử tác
    // động và hành trình đơn hàng
    /*
            if (Objects.equals(request.getTotalPayment(), BigDecimal.ZERO)) {
          Payment savedPayment = paymentRepository.save(buildPayment(PaymentStatus.SUCCESS));
          paymentOrderRepository.saveAll(buildPaymentOrders(savedPayment.getId(), orders));
          orderPort.updateOrderStatus(orderIds, OrderStatus.WAIT_SHIPPING, SubOrderStatus.PREPARING);
          orderTrackingPort.createOrderTracking(orderIds, OrderTrackingStatus.S005);
          writeLogPaymentSuccess(orderIds);

          eventPublisher.publishEvent(new OrdersPaidEvent(this, orders));
          return buildPaymentResponse(savedPayment.getId());
        }

    */
    String GenQR = "a"; // Giả lập genQR
    PaymentMethod paymentMethod = request.getPaymentMethod();
    BigDecimal totalAmount = request.getTotalPayment();
    LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(15);
    String transactionCode = "123"; // Giả lập mã giao dịch

    Payment payment = buildpayment(GenQR, paymentMethod, totalAmount, expiredTime, transactionCode);
    int savepayment = paymentRepostitory.savepayment(payment);
    Integer payentid = payment.getId();
    int savepaymentorder =
        paymentOrderRepostitory.savepaymentorder(buildpaymentorder(payentid, orderlist));
    System.out.println("savepaymentorder" + savepaymentorder);
  }

  // Hàm build response payment
  private Payment buildpayment(
      String GenQR,
      PaymentMethod paymentMethod,
      BigDecimal totalAmount,
      LocalDateTime expiredTime,
      String transactionCode) {
    Payment payment = new Payment();
    payment.setStatus(PaymentStatus.WAIT_PAYMENT);
    payment.setGenQr(GenQR);
    payment.setPaymentMethod(paymentMethod);
    payment.setTotalAmount(totalAmount);
    payment.setTransactionCode(transactionCode);
    payment.setExpiredTime(expiredTime);
    return payment;
  }

  // Hàm build response paymentorder
  private List<Paymentorder> buildpaymentorder(Integer paymentid, List<OrderResponse> orderlist) {

    return orderlist.stream()
        .map(orderResponse -> {
          Paymentorder paymentorder = new Paymentorder();
          paymentorder.setAmount(orderResponse.getTotalAmount());
          paymentorder.setOrderId(orderResponse.getId());
          paymentorder.setPaymentId(paymentid);
          return paymentorder;
        })
        .toList();
  }

  @Override
  public void updatePaymentFromPartnerTest(PaymentupdateRequest request) {
    // Tìm kiếm thng tin thanh toán
    Payment payment = paymentRepostitory.getPaymentById(request.getTransactionCode());
    if (payment == null) {
      throw new BusinessException(MessageCode.ENTITY_NOT_FOUND);
    }
    // Valdate số tiền thanh toán FE gửi lên
    if (payment.getTotalAmount().compareTo(request.getAmount()) != 0) {
      throw new BusinessException(MessageCode.INVALID_TOTAL_AMOUNT);
    }
    paymentRepostitory.updatepayment(
        payment.getId(), request.getTimeTransaction(), PaymentStatus.SUCCESS);
    // Lấy thông tin order của giao dịch
    List<Paymentorder> paymentorder = paymentOrderRepostitory.getOrderByPayment(payment.getId());
    // Lấy danh sách orderID
    List<Integer> orderIds = paymentorder.stream().map(Paymentorder::getOrderId).toList();
    // Lấy danh sách order
    List<OrderResponse> orderInfo = orderPort.getOrderByIds(orderIds);
    //        orderPort.getOrderByIds(orderIds).stream()
    //            .filter(orderResponse -> orderResponse.getStatus() == OrderStatus.WAIT_PAYMENT)
    //            .toList();
    // Lấy danh sách tất cả order sang trạng thái chờ giao hàng
    List<Integer> orderId = orderInfo.stream().map(OrderResponse::getId).toList();
    if (orderId.isEmpty()) {
      throw new BusinessException(MessageCode.ENTITY_NOT_FOUND);
    }
    orderPort.uploadOrder(orderId, OrderStatus.WAIT_SHIPPING, OrderSubStatus.PREPARING);
    orderTrackingPort.creatTracking(orderId, OrderTrackingStatus.S005);
    wirteLogPaymentSuccess(orderInfo);
  }

  public void wirteLogPaymentSuccess(List<OrderResponse> orders) {
    String role = "Buyer";
    String action = "Đơn hàng đã thanh toán thành công, nhà bán đang chuẩn bị hàng";
    String description = "Đơn hàng đã thanh toán thành công, nhà bán đang chuẩn bị hàng";
    for (OrderResponse order : orders) {
      OrderActionLog orderActionLog = new OrderActionLog();
      orderActionLog.setOrder_id(order.getId());
      orderActionLog.setAction(action);
      orderActionLog.setRole_action(role);
      orderActionLog.setDescription(description);
      orderActionLogRepository.saveorder(orderActionLog);
    }
  }
}
