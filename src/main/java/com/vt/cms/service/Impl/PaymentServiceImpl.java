package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.PaymentRequest;

import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public void Paymentwebhooks(PaymentRequest paymentRequest) {

        //1. Validate request
        if (paymentRequest == null ) {
            throw new IllegalArgumentException("Invalid payment request");
        }
        if( paymentRequest.getTransactionCode() == null || paymentRequest.getTransactionCode().isEmpty()) {
            throw new IllegalArgumentException("Transaction code is required");
        }
        if(paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        //2. Find payment by transaction code
        Payment payment = paymentRepostitory.paymentbytransaction(paymentRequest.getTransactionCode());
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found");
        }

        //3. Validate amount
        if (paymentRequest.getAmount().compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
         //4. Update payment
        paymentRepostitory.updatepayment(paymentRequest.getTransactionCode());
        //5. Find order
        Order order = orderRepository.getorderbyid(payment.getOrderId());
        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        //6. update orrder
        order.setStatus(OrderStatus.PREPARING);
        orderRepository.save((order));
    }
}
