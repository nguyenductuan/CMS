package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.OrderItemRequest;
import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.SkuOrderRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.*;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.TrackingStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.*;
import com.vt.cms.service.OrderService;
import com.vt.cms.service.PriceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service

public class OrderServiceImpl implements OrderService {

    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ShippingRepository shippingRepository;
    private OrderTrackingRepository orderTrackingRepository;
    private OrderRepository orderRepository;
    private PriceService priceService;


    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderTrackingRepository orderTrackingRepository,
                            ProductRepository productRepository,
                            OrderItemRepository orderItemRepository,
                            ShippingRepository shippingRepository,
                            PriceService priceService

                            ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.shippingRepository = shippingRepository;
        this.orderTrackingRepository = orderTrackingRepository;
        this.priceService = priceService;


    }

    @Override
    public OrderResponse getdetailorder(long orderId) {
        // 1. Lấy order
        OrderResponse order = orderRepository.getorderbyid(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        return order;
    }

    @Override
    public void cancelOrder(int orderId, String notecancel) {
        Order order = new Order();
        order.setId(orderId);
        order.setOrder_status(OrderStatus.CANCELLED);
        order.setNotecancel(notecancel);
        order.setCancelAt(LocalDateTime.now());
        orderRepository.cancelOrder(order);
    }
    @Override
    public BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request) {
        List<OrderResponse> order = orderRepository.getOrder(request);
        for (OrderResponse o : order) {
            List<OrderItemResponse> items1 = orderRepository.getItemsByOrderId(o.getOrderId());
           // o.setOrderItems(items1);
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
    @Transactional
    public OrderCreateResponse createOrder(OrderRequest request) {

        if (request.getOrder() == null || request.getOrder().isEmpty()) {
            throw new RuntimeException("Order is empty");
        }
//
//        if (request.getShipping() == null || request.getShipping().isEmpty()) {
//            throw new RuntimeException("Shipping information is required");
//        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal priceshipping = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // ==========================================
        // 1. Lấy sản phẩm + SKU và tính tiền sản phẩm //
        // =========================

        for (OrderItemRequest orderItemRequest : request.getOrder()) {
            Integer productId = orderItemRequest.getProductId();
            Integer campainID= orderItemRequest.getCampainId();
            ProductDetail product = productRepository.getproduct1(productId,campainID);

            if (product == null) {
                throw new BusinessException(MessageCode.PRODUCT_NOT_FOUND);

            }
            // Chuyển List SKU -> Map để lookup O(1)
            Map<Integer, SkuResponse> skuMap = product.getSkus()
                    .stream()
                    .collect(Collectors.toMap(
                            SkuResponse::getId,
                            Function.identity()
                    ));


            // Duyệt các SKU trong order
            for (SkuOrderRequest item : orderItemRequest.getItem()) {
                SkuResponse sku = skuMap.get(item.getSkuId());

                if (sku == null) {
                    throw new BusinessException(MessageCode.SKU_NOT_FOUND);
                }

                // ==========================================
                // 3. Check stock
                // ==========================================

                // Kiểm tra số lượng
                if (item.getQuantity() == null || item.getQuantity() <= 0)
                {
                    throw new BusinessException(MessageCode.INVALID_QUANTITY);
                }
                //Check stock
                if (sku.getStock() < item.getQuantity()) {
                    throw  new BusinessException(MessageCode.STOCK_NOT_ENOUGH);

                }
                // ==========================================
                // 4. Lấy giá
                // ==========================================
                BigDecimal price = sku.getPriceDiscountCampaign();
                if (price == null)
                {
                    // Xem lại hàm này
                    price = sku.getPrice();
                }
                if (price == null)
                {
                    throw new BusinessException(MessageCode.PRICE_NOT_FOUND_SKU);
                }

                // Tính tiền SKU
                BigDecimal itemAmount = priceService.calculateItemPrice( price, item.getQuantity() );
                totalAmount = totalAmount.add(itemAmount);
                // ==========================================
                // 6. Chuẩn bị OrderItem
                // ==========================================
                OrderItem orderItem = new OrderItem();

                orderItem.setSku_id(item.getSkuId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(price);
                orderItem.setTotal_price(itemAmount);
                orderItems.add(orderItem);
            }
            Shipping shipping = shippingRepository.detailShipping(orderItemRequest.getShipping_service_code());
            if (shipping == null || shipping.getFee() == null) {
                throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
            }

            if (shipping.getFee().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
            }
            priceshipping = priceshipping.add(shipping.getFee());
        }
        // ==========================================
        // 3. Tính tổng tiền
        // ==========================================
        //Lấy thng tin phuwong thức vận chuyển  request gửi lên

        BigDecimal totalAmountt = totalAmount.add(priceshipping);
        //     2. Tạo order
        Order order = new Order();
        order.setTotal(totalAmountt);
        order.setOrder_status(OrderStatus.WAIT_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
       // order.setExpectedDelivery(LocalDateTime.now().plusDays(2));
        orderRepository.insertorder(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder_id(order.getId());
        }
        // ==========================================
        // 11. Batch insert OrderItem
        // ==========================================
        orderItemRepository.insertorderitems(orderItems);

//       Integer orderid = order.getId();
//        OrderTracking tracking = new OrderTracking();
//        tracking.setOrderId(orderid);
//        tracking.setStatusCode(TrackingStatus.WAITING_PAYMENT.getCode());
//        tracking.setTitle(TrackingStatus.WAITING_PAYMENT.getDescription());
//        orderTrackingRepository.insertordertracking(tracking);
//       // Thêm vào bảng payment
//        Payment payment = new Payment();
//        payment.setOrderId(orderid);
//        payment.setStatus("WAITING_PAYMENT");
//        payment.setAmount(totalAmount);
//        payment.setTrancactioncode( LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
//                "-" +
//                UUID.randomUUID().toString()
//                        .replace("-", "")
//                        .substring(0, 8)
//                        .toUpperCase());


        //paymentRepostitory.insertpayment(payment);
        OrderCreateResponse orderCreateResponse = new OrderCreateResponse();
        orderCreateResponse.setOrderId(order.getId());
        orderCreateResponse.setTotal_payment(totalAmountt);
        orderCreateResponse.setPayment_method("QR Code");
        return orderCreateResponse;

    }
    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2);
    }
}
