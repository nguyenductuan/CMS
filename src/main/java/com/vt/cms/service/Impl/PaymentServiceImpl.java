package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.dto.order.Itemorderdetail;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.PaymentStatus;
import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.repository.PaymentRepostitory;
import com.vt.cms.model.resp.PaymentResponse;
import com.vt.cms.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepostitory paymentRepostitory;
    private final OrderRepository orderRepository;
    private final CampainProductRepository campainProductRepository;

    public PaymentServiceImpl(
            PaymentRepostitory paymentRepostitory,
            OrderRepository orderRepository,
            CampainProductRepository campainProductRepository) {
        this.paymentRepostitory = paymentRepostitory;
        this.orderRepository = orderRepository;
        this.campainProductRepository = campainProductRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentcallback(Paymentcallback paymentcallback) {
        if (paymentcallback == null
                || paymentcallback.getTransactionCode() == null
                || paymentcallback.getTransactionCode().isBlank()
                || paymentcallback.getAmount() == null
                || paymentcallback.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Yêu cầu thanh toán không hợp lệ");
        }

        Payment payment = paymentRepostitory.paymentbytransaction(
                paymentcallback.getTransactionCode());
        if (payment == null) {
            throw new IllegalArgumentException("Không tìm thấy giao dịch thanh toán");
        }
        if (payment.getAmount() == null
                || paymentcallback.getAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ");
        }

        Order order = orderRepository.getorderbyId(payment.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng");
        }

        String paymentStatus = payment.getStatus();
        if (PaymentStatus.PAYMENT_SUCCESS.getCode().equalsIgnoreCase(paymentStatus)
                || "SUCCESS".equalsIgnoreCase(paymentStatus)) {
            log.info("Duplicate payment callback ignored. orderId={}, transactionCode={}",
                    payment.getOrderId(), paymentcallback.getTransactionCode());
            return;
        }
        if (order.getOrder_status() != OrderStatus.WAIT_PAYMENT) {
            throw new IllegalStateException("Đơn hàng không ở trạng thái chờ thanh toán");
        }

        List<Itemorderdetail> orderItems =
                orderRepository.getOrderItemsByOrderId1(payment.getOrderId());
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalStateException("Đơn hàng không có sản phẩm để thanh toán");
        }

        // All writes should participate in the same Spring transaction.
        // Atomic/idempotent stock updates should be enforced by the repository/database.
        for (Itemorderdetail item : orderItems) {
            if (item == null || item.getCampaignId() == null || item.getSkuId() == null
                    || item.getQuantity() <= 0) {
                throw new IllegalStateException("Chi tiết đơn hàng không hợp lệ");
            }
            int updatedRows = campainProductRepository.deductStock(
                    item.getCampaignId(), item.getSkuId(), item.getQuantity());
            if (updatedRows != 1) {
                throw new IllegalStateException("SKU không tồn tại hoặc không đủ tồn kho");
            }
        }

        paymentRepostitory.updatepayment(paymentcallback.getTransactionCode());
        order.setOrder_status(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdated_by("SYSTEM");
        orderRepository.save(order);

        log.info("Payment confirmed. orderId={}, transactionCode={}, amount={}",
                payment.getOrderId(), paymentcallback.getTransactionCode(),
                paymentcallback.getAmount());
    }

    @Override
    public PaymentResponse payment(PaymentRequest paymentRequest) {
        if (paymentRequest == null
                || paymentRequest.getOrderIds() == null
                || paymentRequest.getOrderIds() <= 0
                || paymentRequest.getTotalPayment() == null
                || paymentRequest.getTotalPayment().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Thông tin tạo thanh toán không hợp lệ");
        }

        String paymentId = UUID.randomUUID().toString();
        String transactionCode = "VPO" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 13).toUpperCase();

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(15);

        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderIds());
        payment.setStatus("PENDING");
        payment.setTransactionCode(transactionCode);
        payment.setAmount(paymentRequest.getTotalPayment());
        paymentRepostitory.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setPayment_id(paymentId);
        response.setAmount(paymentRequest.getTotalPayment());
        response.setTransaction_code(transactionCode);
        response.setCreated_qr_time(createdTime);
        response.setExpired_qr_time(expiredTime);
        response.setQr_code(generateQRCode());
        return response;
    }

    public String generateQRCode() {
        return "00020101021238630010A000000727013300069704480119VPO1791609490961CMI0208QRIBFTTA530370454062267945802VN62230819VPO1791609490961CMI63049751";
    }
}
