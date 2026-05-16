package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.OrderItemRequest;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderItem;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderItemResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class OrderServiceImpl implements OrderService {
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ShippingRepository shippingRepository;
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    private OrderRepository orderRepository;


    public OrderServiceImpl(OrderRepository orderRepository, OrderStatusHistoryRepository orderStatusHistoryRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, ShippingRepository shippingRepository, RestClient.Builder builder) {
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
    public void cancelOrder(int orderId, String notecancel) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        order.setNotecancel(notecancel);
        order.setCancelledAt(LocalDateTime.now());
        orderRepository.cancelOrder(order);
    }


    @Override
    public BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request) {
        List<OrderResponse> order = orderRepository.getOrder(request);
        for (OrderResponse o : order) {
            List<OrderItemResponse> items1 = orderRepository.getItemsByOrderId(o.getOrderid());
            o.setOrderItems(items1);
        }
        long totalCount = orderRepository.countorder();
        long totalpage = totalCount / (request.getPageSize());


        // page info
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNo(request.getPageNo());
        pageInfo.setPageSize(request.getPageSize());
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalpage);

        // paging response
        PagingResponse<List<OrderResponse>> pagingResponse = new PagingResponse<>();

        pagingResponse.setPageInfo(pageInfo);
        pagingResponse.setData(order);

        // base response
        BaseResponse<PagingResponse<List<OrderResponse>>> response = new BaseResponse<>();

        response.setMessage("Successful!");
        response.setData(pagingResponse);

        return response;

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
        order.setExpectedDelivery(LocalDateTime.now().plusDays(2));
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
