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
        System.out.println("transactionCode = " + paymentcallback.getTransactionCode());
        Payment payment = paymentRepostitory.paymentbytransaction(paymentcallback.getTransactionCode());
        System.out.println("payment = " + payment);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found");
        }
        System.out.println("payment.orderId = " + payment.getOrderId());

        System.out.println("START find order");
        //3. Validate amount
        if (paymentcallback.getAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
        //4. Update payment
        paymentRepostitory.updatepayment(paymentcallback.getTransactionCode());
        //5. Find order
        Order order = orderRepository.getorderbyId(payment.getOrderId());
        System.out.println("order = " + order);
        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        //6. update orrder
        order.setOrder_status(OrderStatus.PREPARING);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdated_by("SYSTEM");
        orderRepository.save((order));
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
        System.out.println("orderId = " + paymentRequest.getOrderIds());

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
