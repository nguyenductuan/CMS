package com.vt.cms.service.Impl;
import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.dto.order.Itemorderdetail;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.PaymentStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.PaymentResponse;
import com.vt.cms.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepostitory paymentRepostitory;
    private final OrderRepository orderRepository;
    private final CampainRepository campainRepository;
    private final CampainProductRepository campainProductRepository;

    public PaymentServiceImpl(PaymentRepostitory paymentRepostitory,
                              OrderRepository orderRepository, CampainRepository campainRepository, CampainProductRepository campainProductRepository)
    {
        this.paymentRepostitory = paymentRepostitory;
        this.orderRepository = orderRepository;
        this.campainRepository = campainRepository;
        this.campainProductRepository = campainProductRepository;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentcallback(Paymentcallback paymentcallback) {
        //1. Validate request
        if (paymentcallback == null ) {
            throw new IllegalArgumentException("Invalid payment request");
        }
        if( paymentcallback.getTransactionCode() == null || paymentcallback.getTransactionCode().isEmpty()) {
            throw new IllegalArgumentException("Transaction code is required");
        }
        if(paymentcallback.getAmount() == null || paymentcallback.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        //2. Find payment by transaction code
        Payment payment = paymentRepostitory.paymentbytransaction(paymentcallback.getTransactionCode());
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found");
        }
        //3. Validate amount
        if (paymentcallback.getAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        //5. Find order
        Order order = orderRepository.getorderbyId(payment.getOrderId());
        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        // 5. Prevent duplicate callback from deducting stock twice
        if (PaymentStatus.PAYMENT_SUCCESS.getCode().equals(payment.getStatus()) || "SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            return;
        }
        //4. Update payment
        paymentRepostitory.updatepayment(paymentcallback.getTransactionCode());

        List<Itemorderdetail> orderItems = orderRepository.getOrderItemsByOrderId1(payment.getOrderId());

        // 7. Deduct stock for the purchased SKU in Campaign
        for (Itemorderdetail item : orderItems) {

            // Process each item
            int updatedRows =  campainProductRepository.deductStock(item.getCampaignId(),
                    item.getSkuId(), item.getQuantity());

            if (updatedRows == 0) {
                throw new IllegalStateException(
                        "SKU not found or insufficient campaign stock"
                );
            }
        }

        //6. update orrder
        order.setOrder_status(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdated_by("SYSTEM");
        orderRepository.save(order);
        log.info("Payment confirmed. orderId={}, transactionCode={}, amount={}", payment.getOrderId(), paymentcallback.getTransactionCode(), paymentcallback.getAmount());
    }

    @Override
    public PaymentResponse payment(PaymentRequest paymentRequest) {

        // Sinh payment ID dạng số
        String paymentId = String.valueOf(
                System.currentTimeMillis()
                        + new Random().nextInt(100000)
        );

        Payment payment = new Payment();
        // Tạo mã giao dịch mock
        String transactionCode = "VPO"
                + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 13)
                .toUpperCase();

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(15);
        log.info("Creating mock payment. orderId={}, amount={}", paymentRequest.getOrderIds(), paymentRequest.getTotalPayment());

        payment.setOrderId(paymentRequest.getOrderIds());
        payment.setStatus("PENDING");
        payment.setTransactionCode(transactionCode);
        payment.setAmount(paymentRequest.getTotalPayment());
        paymentRepostitory.save(payment);
        PaymentResponse payment1= new PaymentResponse();

        payment1.setPayment_id(paymentId);
        payment1.setAmount(paymentRequest.getTotalPayment());
        payment1.setTransaction_code(transactionCode);
        payment1.setCreated_qr_time(createdTime);
        payment1.setExpired_qr_time(expiredTime);
        payment1.setQr_code(generateQRCode());

        return payment1;
    }
    // Hàm sinh QR code
    public String generateQRCode() {

        return "00020101021238630010A000000727013300069704480119VPO1791609490961CMI0208QRIBFTTA530370454062267945802VN62230819VPO1791609490961CMI63049751";

    }
}
