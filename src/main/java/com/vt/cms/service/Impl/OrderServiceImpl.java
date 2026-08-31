package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.OrderItemRequest;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.TrackingStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderItemResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.OrderService;
import com.vt.cms.service.PriceService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service

public class OrderServiceImpl implements OrderService {

    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ShippingRepository shippingRepository;
    private OrderTrackingRepository orderTrackingRepository;
    private OrderRepository orderRepository;
    private PriceService priceService;
    private PaymentRepostitory paymentRepostitory;


    public OrderServiceImpl(OrderRepository orderRepository, OrderTrackingRepository orderTrackingRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, ShippingRepository shippingRepository, RestClient.Builder builder, PaymentServiceImpl paymentServiceImpl) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.shippingRepository = shippingRepository;
        this.orderTrackingRepository = orderTrackingRepository;
        this.priceService = priceService;
        this.paymentRepostitory = paymentRepostitory;

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
        order.setCancelAt(LocalDateTime.now());
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
        // 1. Lấy thông tin sp gửi lên ở orderItemms
        //2. Tính tiền sp
        //3. Lấy thông tin phí ship đã gửi
        //4. Tính tổng giá tiền
        //5. Thêm vào bảng order với thông tin: orderId, totalamount, status: chờ thanh toán, createdAt, thông tin người nhận
        //6. Thêm vào bảng payment với thông tin: id, id đơn, payment_method, payment_provider, transaction_id, status
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest orderItemRequest : request.getOrder()) {
            Integer productId = orderItemRequest.getProductId();
            ProductResponse product = productRepository.detailProduct(productId);

            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            BigDecimal total = priceService.calculateItemPrice(product.getPrice(), orderItemRequest.getQuantity());
            totalAmount = totalAmount.add(total);


        //Lấy thng tin phuwong thức vận chuyển  request gửi lên
        Shipping shipping = shippingRepository.detailShipping(orderItemRequest.getShippingMethodId());
        BigDecimal priceshipping = shipping.getFee();
        totalAmount = totalAmount.add(priceshipping);
    }
        //     2. Tạo order
        Order order = new Order();
        order.setTotal(totalAmount);
        order.setStatus(OrderStatus.WAIT_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
        order.setExpectedDelivery(LocalDateTime.now().plusDays(2));
        orderRepository.insertorder(order);

        var orderid = order.getId();
        OrderTracking tracking = new OrderTracking();
        tracking.setOrderId(orderid);
        tracking.setStatus(TrackingStatus.WAITING_PAYMENT.getCode());
        tracking.setTitle(TrackingStatus.WAITING_PAYMENT.getDescription());
        orderTrackingRepository.insertordertracking(tracking);
       // Thêm vào bảng payment
        Payment payment = new Payment();
        payment.setOrderid(String.valueOf(orderid));
        payment.setStatus("WAITING_PAYMENT");
        payment.setAmount(totalAmount);
        payment.setTrancactioncode( LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                "-" +
                UUID.randomUUID().toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase());


        paymentRepostitory.insertpayment(payment);


//        for (OrderItem o : items) {
//            o.setOrderId(orderid);
//            orderItemRepository.insertorderCartItem(o);
//        }


    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2);
    }
}
