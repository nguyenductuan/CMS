package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.PaymentRequest;

import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.model.resp.PaymentResponse;
import com.vt.cms.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepostitory paymentRepostitory;
    private final OrderRepository orderRepository;
    public PaymentServiceImpl(PaymentRepostitory paymentRepostitory,
                              OrderRepository orderRepository)
    {
        this.paymentRepostitory = paymentRepostitory;
        this.orderRepository = orderRepository;
    }

    @Override
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
        //4. Update payment
        paymentRepostitory.updatepayment(paymentcallback.getTransactionCode());
        //5. Find order
        Order order = orderRepository.getorderbyId(payment.getOrderId());
        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        //6. update orrder
        order.setOrder_status(OrderStatus.PREPARING);
        orderRepository.save((order));
    }

    @Override
    public PaymentResponse payment(PaymentRequest paymentRequest) {

        // Sinh payment ID dạng số
        String paymentId = String.valueOf(
                System.currentTimeMillis()
                        + new Random().nextInt(100000)
        );

        PaymentResponse payment = new PaymentResponse();
        // Tạo mã giao dịch mock
        String transactionCode = "VPO"
                + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 13)
                .toUpperCase();

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(15);
        payment.setPayment_id(paymentId);
        payment.setAmount(paymentRequest.getTotalPayment());
        payment.setTransaction_code(transactionCode);
        payment.setCreated_qr_time(createdTime);
        payment.setExpired_qr_time(expiredTime);
        payment.setQr_code(generateQRCode());

        return payment;
    }
    // Hàm sinh QR code
    public String generateQRCode() {

        return "00020101021238630010A000000727013300069704480119VPO1791609490961CMI0208QRIBFTTA530370454062267945802VN62230819VPO1791609490961CMI63049751";

    }
}
