package com.vt.cms.order.application.service.impl;

import com.vt.cms.order.application.dto.PreparedOrderRequest;
import com.vt.cms.order.application.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    public void preparedOrder(PreparedOrderRequest request, boolean isSellerOrder) {

    }
//            PreparedOrderRequest request, boolean isSellerOrder) {
//                String requestId = RequestUtils.extractRequestId();
//                List<Order> orders =
//                        orderRepository.findAllByIdsAndSubOrderStatus(
//                                request.getOrderId(), SubOrderStatus.PREPARING);
//                if (orders.size() != request.getOrderId().size()) {
//                    throw new BaseException(CommonErrorDef.ENTITY_NOT_FOUND);
//                }
//                List<OrderShipping> orderShippings =
//                        orderShippingRepository.findAllByOrderIds(request.getOrderId());
//                Map<Long, Order> orderMap =
//                        orders.stream().collect(Collectors.toMap(Order::getId, Function.identity()));
//                for (OrderShipping orderShipping : orderShippings) {
//                    Order order = orderMap.get(orderShipping.getOrderId());
//                    ShippingServiceRequest shippingServiceRequest =
//                            ShippingServiceRequest.builder()
//                                    .senderWard(request.getSellerInfo().getWarehouseWardId())
//                                    .senderProvince(request.getSellerInfo().getWarehouseProvinceId())
//                                    .receiverWard(Integer.valueOf(orderShipping.getBuyerWardId()))
//                                    .receiverProvince(Integer.valueOf(orderShipping.getBuyerProvinceId()))
//                                    .productWeight(order.getTotalWeight())
//                                    .productPrice(order.getTotalItemAmount().longValue())
//                                    .build();
//                    List<ShippingServiceResponse> services =
//                            shippingPartnerGateway.getAvailableServices(requestId, shippingServiceRequest);
//                    Map<String, ShippingServiceResponse> serviceMap =
//                            services.stream()
//                                    .collect(
//                                            Collectors.toMap(ShippingServiceResponse::getServiceCode, Function.identity()));
//                    ShippingServiceResponse service = serviceMap.get(order.getShippingServiceCode());
//                    orderShipping.setSellerActualName(request.getSellerInfo().getFullName());
//                    orderShipping.setSellerActualPhone(request.getSellerInfo().getPhoneNumber());
//                    orderShipping.setSellerActualAddress(request.getSellerInfo().getWarehouseAddress());
//                    orderShipping.setSellerActualProvinceId(
//                            String.valueOf(request.getSellerInfo().getWarehouseProvinceId()));
//                    orderShipping.setSellerActualWardId(
//                            String.valueOf(request.getSellerInfo().getWarehouseWardId()));
//                    orderShipping.setSellerActualShippingFee(BigDecimal.valueOf(service.getFee()));
//                    orderShipping.setShippingNote(request.getNote());
//                }
//                orders.forEach(order -> order.setSubOrderStatus(SubOrderStatus.PENDING_SHIPMENT));
//                orderRepository.saveAll(orders);
//                orderShippingRepository.saveAll(orderShippings);
//                sendOrderToKafka(orders, createOrderTopic);
//                writeLogPrepare(isSellerOrder, orders);
//                return new PreparedOrderResponse(
//                        orders.stream().map(Order::getId).toList(),
//                        request.getOrderId().size(),
//                        request.getOrderId().size());
//
//}
//            private void writeLogPrepare(boolean isSellerOrder, List<Order> orders) {
//                for (Order order : orders) {
//                    orderActionSendLog.sendOrderActionLog(
//                            OrderActionLogEvent.builder()
//                                    .orderId(order.getId())
//                                    .createdAt(LocalDateTime.now())
//                                    .roleAction(isSellerOrder ? RoleAction.SELLER : RoleAction.ADMIN)
//                                    .action(
//                                            isSellerOrder
//                                                    ? "Seller xác nhận đã chuẩn bị hàng thành công"
//                                                    : "Admin xác nhận đã chuẩn bị hàng thành công")
//                                    .description(
//                                            isSellerOrder
//                                                    ? "Seller xác nhận đã chuẩn bị hàng thành công"
//                                                    : "Admin xác nhận đã chuẩn bị hàng thành công")
//                                    .build());
//                }
            }

