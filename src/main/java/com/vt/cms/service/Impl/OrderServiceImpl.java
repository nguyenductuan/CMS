package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.OrderItemRequest;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderItem;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.OrderItemResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class OrderServiceImpl implements OrderService {
    final private OrderRepository orderRepository;
    final private ProductRepository productRepository;
    final private OrderItemRepository orderItemRepository;
    final private ShippingRepository shippingRepository;
    final private OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderStatusHistoryRepository orderStatusHistoryRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, ShippingRepository shippingRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.shippingRepository = shippingRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;

    }

    @Override
    public OrderResponse getdetailorder(long orderId) {
        // 1. Lấy order
        OrderResponse order = orderRepository.getorderbyid(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        // 2. Lấy list items
        List<OrderItemResponse> items = orderRepository.getItemsByOrderId(orderId);
        // 3. Set vào order
        order.setOrderItems(items);
        return order;
    }

    @Override
    public void cancelOrder(int orderId) {
        //OrderResponse order = orderRepository.getItemsByOrderId(orderId);
        //  order.setStatus(OrderStatus.CANCELLED);
        //order.setCancelAt(LocalDateTime.now());
        //  orderRepository.cancelOrder(order);
    }

    @Override
    public List<OrderResponse> getorderlist() {
        List<OrderResponse> order = orderRepository.getOrder();
        for (OrderResponse o : order) {
            List<OrderItemResponse> items1 = orderRepository.getItemsByOrderId(o.getOrderid());
            o.setOrderItems(items1);
        }
        return order;
    }

    @Override
    public void createOrder(OrderRequest request) {
//        1. Tính tiền
//        1.1 Tính tổng tiền của sản phẩm
        int subtotal = 0;
        double totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            Product product = productRepository.detailProduct(orderItemRequest.getProductId());
            subtotal = product.getPrice() * orderItemRequest.getQuantity();
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(subtotal);
            items.add(orderItem);
            totalPrice += subtotal;
        }
//       1.2 Lay tien ship qua id
        Shipping shipping = shippingRepository.detailShipping(request.getShippingMethodId());
        double shipprice = shipping.getFee();
//       1.3. Tính tổng tiền
        double total = totalPrice + shipprice;
//       2. Tạo order
        Order order = new Order();
        order.setTotal(total);
        order.setStatus(OrderStatus.WAIT_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
        order.setExpectedDelivery(getEstimatedDeliveryTime());
        orderRepository.insertorder(order);
        var orderid = order.getId();
        String title = "Tạo mới đơn hàng";
        // Insert thêm bản ghi vào OrderHistory
        orderStatusHistoryRepository.insertorderByStatus(orderid, order.getStatus(), title);

        for (OrderItem o : items) {
            o.setOrderId(orderid);
            orderItemRepository.insertorderCartItem(o);
        }
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2);
    }
}
