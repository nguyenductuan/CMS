package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderService;
import com.vt.cms.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private OrderService orderService;
    private OrderRepository orderRepository;


    public PaymentServiceImpl(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void ProcessPayment(PaymentRequest paymentRequest) {
        //1. Lấy ra trạng thái của order
        OrderResponse order = orderService.getdetailorder(paymentRequest.getOrderId());
        if (order == null) {
            throw new RuntimeException("Không tìm thấy order");
        }
        if (paymentRequest.getResult().equals("success")) {
            //order.setStatus(OrderStatus.CONFIRMED);
//            order.setPaymentStatus(OrderStatus.WAITING_FOR_DELIVERY);
//            orderRepository.save(order);

        } else {
            //order.setStatus(OrderStatus.FAILED);
//            orderRepository.save(order);
        }


    }
}
