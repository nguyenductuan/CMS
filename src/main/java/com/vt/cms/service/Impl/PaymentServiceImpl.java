package com.vt.cms.service.Impl;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.PaymentCallback;
import com.vt.cms.model.dto.order.Itemorderdetail;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.PaymentStatus;
import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.repository.PaymentRepository;
import com.vt.cms.model.resp.PaymentResponse;
import com.vt.cms.service.PaymentService;
import com.vt.cms.service.OrderHistoryService;
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
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CampainProductRepository campainProductRepository;
    private final OrderHistoryService orderHistoryService;

    /**
     * Xử lý thanh toán payment service impl. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            CampainProductRepository campainProductRepository,
            OrderHistoryService orderHistoryService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.campainProductRepository = campainProductRepository;
        this.orderHistoryService = orderHistoryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * Xử lý thanh toán paymentCallback. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void paymentCallback(PaymentCallback paymentCallback) {
        if (paymentCallback == null
                || paymentCallback.getTransactionCode() == null
                || paymentCallback.getTransactionCode().isBlank()
                || paymentCallback.getAmount() == null
                || paymentCallback.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.YEU_CAU_THANH_TOAN_KHONG_HOP_LE);
        }

        Payment paymentLookup = paymentRepository.findByTransactionCode(
                paymentCallback.getTransactionCode());
        if (paymentLookup == null) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_GIAO_DICH_THANH_TOAN);
        }

        // Lock order first to serialize different payment sessions for the same order.
        Order order = orderRepository.getorderbyIdForUpdate(paymentLookup.getOrderId());
        if (order == null) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_DON_HANG);
        }
        Payment payment = paymentRepository.findByTransactionCodeForUpdate(
                paymentCallback.getTransactionCode());
        if (payment == null) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_GIAO_DICH_THANH_TOAN);
        }
        if (payment.getAmount() == null
                || paymentCallback.getAmount().compareTo(payment.getAmount()) != 0) {
            throw new BusinessException(MessageCode.SO_TIEN_THANH_TOAN_KHONG_HOP_LE);
        }
        if (order.getTotal() == null
                || paymentCallback.getAmount().compareTo(order.getTotal()) != 0) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.SO_TIEN_CALLBACK_KHONG_KHOP_TONG_TIEN_DON_HANG);
        }

        String paymentStatus = payment.getStatus();
        if ("SUCCESS".equalsIgnoreCase(paymentStatus)
                || PaymentStatus.PAYMENT_SUCCESS.getCode().equalsIgnoreCase(paymentStatus)) {
            log.info("Duplicate payment callback ignored. orderId={}, transactionCode={}",
                    payment.getOrderId(), paymentCallback.getTransactionCode());
            return;
        }
        if (!"PENDING".equalsIgnoreCase(paymentStatus)) {
            throw new IllegalStateException("Giao dịch không ở trạng thái chờ thanh toán");
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

        if (paymentRepository.markSuccessIfPending(paymentCallback.getTransactionCode()) != 1) {
            throw new IllegalStateException("Giao dịch không còn ở trạng thái chờ thanh toán");
        }
        order.setOrder_status(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdated_by("SYSTEM");
        orderRepository.save(order);
        orderHistoryService.record(order.getId(), "005", "PAID", "SYSTEM", "SYSTEM", "Đơn hàng đã thanh toán thành công, Nhà bán đang chuẩn bị hàng", "Cổng thanh toán xác nhận giao dịch thành công");

        log.info("Payment confirmed. orderId={}, transactionCode={}, amount={}",
                payment.getOrderId(), paymentCallback.getTransactionCode(),
                paymentCallback.getAmount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * Xử lý thanh toán payment. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public PaymentResponse payment(PaymentRequest paymentRequest) {
        if (paymentRequest == null
                || paymentRequest.getOrderIds() == null
                || paymentRequest.getOrderIds() <= 0
                || paymentRequest.getTotalPayment() == null
                || paymentRequest.getTotalPayment().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(MessageCode.THONG_TIN_TAO_THANH_TOAN_KHONG_HOP_LE);
        }

        Order order = orderRepository.getorderbyIdForUpdate(paymentRequest.getOrderIds());
        if (order == null) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_DON_HANG);
        }
        if (order.getOrder_status() != OrderStatus.WAIT_PAYMENT) {
            throw new IllegalStateException("Đơn hàng không ở trạng thái chờ thanh toán");
        }
        if (order.getTotal() == null
                || order.getTotal().compareTo(paymentRequest.getTotalPayment()) != 0) {
            throw new BusinessException(MessageCode.SO_TIEN_THANH_TOAN_KHONG_KHOP_TONG_TIEN_DON_HANG);
        }
        if (paymentRepository.countPendingByOrderId(order.getId()) > 0) {
            throw new IllegalStateException("Đơn hàng đã có giao dịch đang chờ thanh toán");
        }

        String transactionCode = "VPO" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 13).toUpperCase();

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(15);

        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderIds());
        payment.setStatus("PENDING");
        payment.setTransactionCode(transactionCode);
        payment.setAmount(paymentRequest.getTotalPayment());
        int insertedRows = paymentRepository.save(payment);
        if (insertedRows != 1 || payment.getId() == null) {
            throw new IllegalStateException("Không thể tạo bản ghi thanh toán");
        }

        PaymentResponse response = new PaymentResponse();
        response.setPayment_id(String.valueOf(payment.getId()));
        response.setAmount(paymentRequest.getTotalPayment());
        response.setTransaction_code(transactionCode);
        response.setCreated_qr_time(createdTime);
        response.setExpired_qr_time(expiredTime);
        response.setQr_code(generateQRCode());
        return response;
    }

    /**
     * Thực hiện xử lý nghiệp vụ generate qrcode. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public String generateQRCode() {
        return "00020101021238630010A000000727013300069704480119VPO1791609490961CMI0208QRIBFTTA530370454062267945802VN62230819VPO1791609490961CMI63049751";
    }
}
