package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.OrderShippingRequest;
import com.vt.cms.model.entity.*;
import com.vt.cms.model.enums.OrderShippingStatus;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.enums.TrackingStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderShippingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderShippingServiceImpl implements OrderShippingService {
    WarehouseRepository warehouseRepository;
    OrderRepository orderRepository;
    ShippingRepository shippingRepository;
    OrderShippingRepository orderShippingRepository;
    ShiperRepository shiperRepository;
    OrderTrackingRepository orderTrackingRepository;

    public OrderShippingServiceImpl(WarehouseRepository warehouseRepository,
                                    OrderRepository orderRepository,
                                    ShippingRepository shippingRepository,
                                    OrderTrackingRepository orderstatusHistory,
                                    OrderShippingRepository orderShippingRepository,
                                    ShiperRepository shiperRepository,
                                    OrderTrackingRepository orderTrackingRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.orderRepository = orderRepository;
        this.shippingRepository = shippingRepository;

        this.orderShippingRepository = orderShippingRepository;
        this.shiperRepository = shiperRepository;
        this.orderTrackingRepository = orderTrackingRepository;
    }

    @Override
    public OrderShipping prepare(OrderShippingRequest orderShippingRequest) {
        List<Warehouse> warehouse = warehouseRepository.getlistwahouse();
        String warehouseCode = orderShippingRequest.getSellerInfos().get(0).getWarehousecode();
        if ( warehouseCode == null) {
            throw new RuntimeException("Warehousecode không được để trống");
        }
        Warehouse warehouse1 = warehouse
                .stream()
                .filter(w -> w.getWarehouse_code().equals(warehouseCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho với warehousecode: " + warehouseCode));
        List<Order>orders = new ArrayList<>();
        for (Order o : orderShippingRequest.getOrders()) {
            if (o.getId() == null) {
                throw new RuntimeException("OrderId không được để trống");
            }
            Order order = orderRepository.getorderbyid(o.getId());
            if (order.getStatus() != OrderStatus.PAID){
                throw new RuntimeException("Đơn hàng khác trạng thái đã thanh toán, không thể chuẩn bị hàng");
            }
            if (order.getId() == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng với orderid: " + o.getId());
            }
         orders.add(order);
        }
        //2. Tạo shipperment gán orderid và washercode
        OrderShipping orderShipping = new OrderShipping();
        for (Order order : orders) {
            orderShipping.setOrderId(order.getId());
            orderShipping.setWarehouseName(warehouse1.getWarehouse_name());
            orderShipping.setWarehouseCode(warehouse1.getWarehouse_code());
            orderShipping.setTrackingCode(genTracking());
            orderShipping.setStatus(OrderShippingStatus.WAIT_SHIPPING);
            orderShipping.setOrderAmount(order.getTotal());
            orderShipping.setShippingFee(calculateShipperFree(orderShippingRequest, order));
            BigDecimal totalAmount = orderShipping.getOrderAmount().add(orderShipping.getShippingFee());
            orderShipping.setTotalAmount(totalAmount);
            orderShipping.setCreatedAt(LocalDateTime.now());
            Order order1 =orderRepository.getorderbyid(order.getId());
            order1.setStatus(OrderStatus.PREPARING);
            orderRepository.save(order1);
            orderShipping.setDeliveredAt(order1.getDeliveredAt());
            orderShippingRepository.saveShipment(orderShipping);

            OrderTracking orderTracking = new OrderTracking();
            orderTracking.setOrderId(order.getId());
            orderTracking.setStatusCode(TrackingStatus.CONFIRMED.getCode());
            orderTracking.setStatusName(TrackingStatus.CONFIRMED.getName());
            orderTracking.setDescription(TrackingStatus.CONFIRMED.getDescription());

            orderTrackingRepository.insertordertracking(orderTracking);
        }
        return orderShipping;
    }
    //Hàm tính phí vận chuyển
    public BigDecimal calculateShipperFree(OrderShippingRequest orderShippingRequest, Order order) {
        //Phí vận chuyển = phí cân năng + Phí khoảng cách
        BigDecimal shippingKm= shippingKm(orderShippingRequest,order);
        BigDecimal shippWeight= shippingWeight(order);
        return shippingKm.add(shippWeight);
    }
    //Hàm tính phí khoảng cách
    public  BigDecimal shippingKm(OrderShippingRequest orderShippingRequest, Order order) {
        double distance = calculateDistance(
                orderShippingRequest.getSellerInfos().get(0).getLatitude(),
                orderShippingRequest.getSellerInfos().get(0).getLongitude(),
                order.getDeliveryLatitude(),
                order.getDeliveryLongitude()
        );
        return calculateShippingFee(distance);
    }
    private BigDecimal calculateShippingFee(double distance) {

        BigDecimal baseFee = BigDecimal.valueOf(15_000);
        BigDecimal pricePerKm = BigDecimal.valueOf(5_000);

        // 2 km đầu: 15.000đ
        if (distance <= 2) {
            return baseFee;
        }

        // Từ km thứ 3: 5.000đ/km
        return baseFee.add(
                pricePerKm.multiply(
                        BigDecimal.valueOf(distance - 2)
                )
        );
    }
    private double calculateDistance(
            double lat1,double lon1,
            double lat2,double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );
        return EARTH_RADIUS_KM * c;

    }
    //Hàm tính phí cân nặng
    public  BigDecimal shippingWeight(Order order){
BigDecimal a = order.getProductheight().multiply(order.getProductlength()).multiply(order.getProductwidth());
BigDecimal b = a.divide(BigDecimal.valueOf(1000));
        BigDecimal weight = b.compareTo(order.getProductweight()) > 0
                ? a
                : order.getProductweight();
        if (weight != null) {
            BigDecimal baseFee = BigDecimal.valueOf(10_000);
            BigDecimal pricePerKg = BigDecimal.valueOf(2_000);
            // 1 kg đầu: 10.000đ
            if (weight.compareTo(BigDecimal.ONE) <= 0) {
                return baseFee;
            }
            // Từ kg thứ 2: 2.000đ/kg
            return baseFee.add(
                    pricePerKg.multiply(
                            weight.subtract(BigDecimal.ONE)
                    )
            );
        }
            throw new RuntimeException("Không thể tính phí vận chuyển do không có thông tin cân nặng của đơn hàng");
    }

 @Transactional
    public OrderShipping assignShipper(String trackingCode) {
        // Lấy danh sách shiper AVAILABLE -> shiperment set tên shiper,
        // set thời gian dự kiến, set status DELIVERING
        //shiper set status thành BUSY
        //order set status thành DELIVERING
        Shipper shipper = shiperRepository.getlistShipper();

        OrderShipping orderShipping = orderShippingRepository.getShipmentByTrackingcode(trackingCode);
        if (orderShipping == null) {
            throw new RuntimeException("Không tìm thấy mã đơn hàng: " + trackingCode);
        }
        if (shipper == null || shipper.getId() == null) {
            throw new RuntimeException("Không tìm thấy shipper available");
        }
        orderShipping.setShipperId(shipper.getId());

        orderShipping.setEstimatedDeliveryTime(getEstimatedDeliveryTime());
        orderShipping.setStatus(OrderShippingStatus.SHIPPING);
        shipper.setStatus("BUSY");
        orderShippingRepository.saveShipment(orderShipping);
        shiperRepository.saveShiper(shipper);

     Order order = orderRepository.getorderbyid(
             orderShipping.getOrderId()
     );

     order.setStatus(OrderStatus.SHIPPING);
     orderRepository.save(order);
        //Lưu order
        orderRepository.save(order);

        // Gán thông tin vào bảng orderTracking
        OrderTracking orderTracking = new OrderTracking();
        orderTracking.setOrderId(orderShipping.getOrderId());
        orderTracking.setStatusCode(TrackingStatus.SHIPPING.getCode());
        orderTracking.setStatusName(TrackingStatus.SHIPPING.getName());
        orderTracking.setDescription(TrackingStatus.SHIPPING.getDescription());
        orderTrackingRepository.insertordertracking(orderTracking);
        return orderShipping;

    }

    @Override
    public void shipeperdelivery(int orderid) {
        Order order1 = new Order();
        order1.setId(orderid);
        order1.setSubstatus(OrderStatus.SHIPPING);
        order1.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order1);
        OrderResponse orderResponse = orderRepository.getorderbyid(orderid);
        String title = "Shipper giao hàng cho khách hàng";
        orderstatusHistory.insertorderByStatus(orderid, orderResponse.getOrderStatus(), title);
    }

    @Override
    public OrderResponse confirmReceived(int orderid) {
        //Set status DELIVERED, set thời gian nhận hàng
        OrderResponse order = orderRepository.getorderbyid(orderid);
        Order order3 = new Order();
        order3.setId(orderid);
        order3.setStatus(OrderStatus.DELIVERED);
        order3.setSubstatus(OrderStatus.DELIVERED);
        order3.setDeliveredAt(LocalDateTime.now());
        orderRepository.save(order3);
        OrderResponse orderResponse = orderRepository.getorderbyid(orderid);
        String title = "Khách hàng đã nhận hàng";
        orderstatusHistory.insertorderByStatus(orderid, orderResponse.getOrderStatus(), title);
        return order;
    }

    //Hàm sinh mã vâ đơn
    public String genTracking() {
        return "VTP" + System.currentTimeMillis();
    }
    //Hàm tính thời gian giao dự kiến
    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2); // check lại không nhận hàm này khi lấy thời gian ở trên
    }
}
