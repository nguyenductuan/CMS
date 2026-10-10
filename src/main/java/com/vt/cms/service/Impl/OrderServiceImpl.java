package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.*;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderItem;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.OrderItemRepository;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderCreateResponse;
import com.vt.cms.model.resp.OrderItemResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.model.resp.SkuResponse;
import com.vt.cms.service.InventoryService;
import com.vt.cms.service.OrderPricingService;
import com.vt.cms.service.OrderService;
import com.vt.cms.service.ShippingFeeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final OrderPricingService orderPricingService;
    private final ShippingFeeService shippingFeeService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository,
            InventoryService inventoryService,
            OrderPricingService orderPricingService,
            ShippingFeeService shippingFeeService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryService = inventoryService;
        this.orderPricingService = orderPricingService;
        this.shippingFeeService = shippingFeeService;
    }

    @Override
    public OrderResponse getdetailorder(long orderId) {
         OrderResponse order = orderRepository.getorderbyid(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        // Lấy danh sách item của order
        List<ItemDto> orderItems = orderRepository.getOrderItemsByOrderId(orderId);
        order.setOrderItems(orderItems);
        System.out.println("Order ID: " + order);
        return order;
    }

    @Override
    public void cancelOrder(int orderId, String notecancel) {
        Order order = new Order();
        order.setId(orderId);
        order.setOrder_status(OrderStatus.CANCELLED);
        order.setNotecancel(notecancel);
        order.setUpdatedAt(LocalDateTime.now());
        order.setCancelled_by("SYSTEM");
        orderRepository.cancelOrder(order);
    }

    @Override
    public BaseResponse<PagingResponse<List<OrderResponse>>> getorderlist(OrdersRequest request) {
        List<OrderResponse> orders = orderRepository.getOrder(request);
        System.out.println("Orders: " + orders);
        for (OrderResponse order : orders) {
            List<OrderItemResponse> items = orderRepository.getItemsByOrderId(order.getOrderId());

            // Map OrderItemResponse sang ItemDto
            List<ItemDto> itemDtos = items.stream()
                    .map(item -> {
                        ItemDto dto = new ItemDto();

                        // Đổi tên getter/setter theo class thực tế của bạn
                        dto.setProductId(item.getProductId());
                        dto.setProductName(item.getProductName());
//                        dto.setPrice(item.getPrice());
                        dto.setQuantity(item.getQuantity());

                        return dto;
                    })
                    .toList();

            // Gắn danh sách item vào order
            order.setOrderItems(itemDtos);
        }

        long totalCount = orderRepository.countorder();
        long pageSize = request.getPageSize();
        long totalPage = pageSize > 0
                ? (totalCount + pageSize - 1) / pageSize
                : 0;

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNo(request.getPageNo());
        pageInfo.setPageSize(request.getPageSize());
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalPage);

        PagingResponse<List<OrderResponse>> pagingResponse = new PagingResponse<>();
        pagingResponse.setPageInfo(pageInfo);
        pagingResponse.setData(orders);

        BaseResponse<PagingResponse<List<OrderResponse>>> response = new BaseResponse<>();
        response.setMessage("Successful!");
        response.setData(pagingResponse);
        return response;
    }

    /**
     * Coordinates the create-order use case. The participating repository writes
     * must use the same transaction manager/data source for atomic persistence.
     */
    @Override
    @Transactional
    public OrderCreateResponse createOrder(OrderRequest request) {
        if (request == null || request.getOrder() == null || request.getOrder().isEmpty()) {
            throw new BusinessException(MessageCode.INVALID_ORDER);
        }

        BigDecimal productTotal = BigDecimal.ZERO;
        BigDecimal shippingTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getOrder()) {
            if (itemRequest == null || itemRequest.getProductId() == null
                    || itemRequest.getItem() == null || itemRequest.getItem().isEmpty()) {
                throw new BusinessException(MessageCode.INVALID_ORDER);
            }

            ProductDetail product = productRepository.getproduct1(
                    itemRequest.getProductId(),
                    itemRequest.getCampainId());

            if (product == null) {
                throw new BusinessException(MessageCode.PRODUCT_NOT_FOUND);
            }

            Map<Integer, SkuResponse> skuById =
                    inventoryService.validateRequestedItems(product, itemRequest.getItem());

            for (SkuOrderRequest requestedSku : itemRequest.getItem()) {
                SkuResponse sku = skuById.get(requestedSku.getSkuId());
                BigDecimal unitPrice = orderPricingService.getUnitPrice(sku);
                BigDecimal lineTotal = orderPricingService.calculateLineTotal(
                        unitPrice,
                        requestedSku.getQuantity());

                productTotal = productTotal.add(lineTotal);

                OrderItem orderItem = new OrderItem();
                orderItem.setSku_id(requestedSku.getSkuId());
                orderItem.setQuantity(requestedSku.getQuantity());
                orderItem.setPrice(unitPrice);
                orderItem.setTotal_price(lineTotal);
                orderItem.setProduct_id(product.getId());
                orderItems.add(orderItem);
            }

            // Preserves the current behavior: one shipping fee per request group.
            // Confirm this rule with the business before changing fee aggregation.
            shippingTotal = shippingTotal.add(
                    shippingFeeService.calculateFee(itemRequest.getShipping_service_code()));
        }

        BigDecimal grandTotal = productTotal.add(shippingTotal);
        Order order = new Order();
        order.setTotal(grandTotal);
        order.setOrder_status(OrderStatus.WAIT_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
        order.setExpectedDelivery(getEstimatedDeliveryTime());
        order.setCreated_by("SYSTEM");


        int insertedOrders = orderRepository.insertorder(order);
        if (insertedOrders <= 0 || order.getId() == null) {
            throw new BusinessException(MessageCode.INVALID_ORDER);
        }

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder_id(order.getId());
        }

        int insertedItems = orderItemRepository.insertorderitems(orderItems);
        if (insertedItems <= 0) {
            throw new BusinessException(MessageCode.INVALID_ORDER);
        }

        OrderCreateResponse response = new OrderCreateResponse();
        response.setOrderId(order.getId());
        response.setTotal_payment(grandTotal);
        // Kept for backward compatibility. This does not mean a payment was created.
        response.setPayment_method("QR Code");
        return response;
    }
    //Hàm tính thời gian giao hàng
    public LocalDateTime getEstimatedDeliveryTime() {

        return LocalDateTime.now().plusDays(7); // Example: 7 days from now
    }
}
