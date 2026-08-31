package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.PaymentRequest;

import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepostitory paymentRepostitory;
    private final OrderRepository orderRepository;
    public PaymentServiceImpl(PaymentRepostitory paymentRepostitory, OrderRepository orderRepository) {
        this.paymentRepostitory = paymentRepostitory;
        this.orderRepository = orderRepository;
    }
    @Override
    public void Paymentwebhooks(PaymentRequest paymentRequest) {
        paymentRepostitory.updatepayment(paymentRequest.getTransactionCode());
        Payment payment =  paymentRepostitory.paymentbytransaction(paymentRequest.getTransactionCode());
        Order order = orderRepository.getItemsByOrderId(payment.getOrderid());
        if (order != null) {
            order.setStatus(OrderStatus.PREPARING);
            orderRepository.updateorderstatus(order);
        }
    }
}
