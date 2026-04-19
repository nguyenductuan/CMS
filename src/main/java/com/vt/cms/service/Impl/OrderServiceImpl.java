package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.OrderItemRequest;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderItem;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.repository.OrderItemRepository;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.repository.ShippingRepository;
import com.vt.cms.service.OrderService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service

public class OrderServiceImpl implements OrderService {
    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ShippingRepository shippingRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderItemRepository orderItemRepository,
                            ShippingRepository shippingRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.shippingRepository = shippingRepository;

    }

    @Override
    public void createOrder(OrderRequest request) {
//        1. Tính tiền
//                1.1 Tính tổng tiền của sản phẩm
        double subtotal = 0;
        double totalPrice = 0;
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            Product product = productRepository.detailProduct(orderItemRequest.getProductId());
            subtotal = product.getPrice() * orderItemRequest.getQuantity();
            totalPrice += subtotal;

        }
//                1.2 Lay tien ship qua id
        Shipping shipping = shippingRepository.detailShipping(request.getShippingMethodId());
        double shipprice = shipping.getFee();
//                1.3. Tính tổng tiền
        double total = totalPrice + shipprice;
//        2. Tạo order
        Order order = new Order();
        order.setTotal(total);
        orderRepository.insertorder(order);
        var orderid = order.getId();
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderid);
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItemRepository.insertorderCartItem(orderItem);
        }
    }
}
