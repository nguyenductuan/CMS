package com.vt.cms.service.Impl;

import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Shipment;
import com.vt.cms.model.entity.Shipper;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.DeliveryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    WarehouseRepository warehouseRepository;
    OrderRepository orderRepository;
    ShippingRepository shippingRepository;
    ShipmentRepository shipmentRepository;
    OrderStatusHistoryRepository orderstatusHistory;
    ShiperRepository shiperRepository;

    public DeliveryServiceImpl(WarehouseRepository warehouseRepository,
                               OrderRepository orderRepository,
                               ShippingRepository shippingRepository,
                               OrderStatusHistoryRepository orderstatusHistory,
                               ShipmentRepository shipmentRepository,
                               ShiperRepository shiperRepository) {
        this.warehouseRepository = warehouseRepository;
        this.orderRepository = orderRepository;
        this.shippingRepository = shippingRepository;
        this.orderstatusHistory = orderstatusHistory;
        this.shipmentRepository = shipmentRepository;
        this.shiperRepository = shiperRepository;
    }

    @Override
    public Shipment prepare(Integer orderid, Integer warehousecode) {
        List<Warehouse> warehouse = warehouseRepository.getlistwahouse();
        //1. Tìm kho theo id kho
        Warehouse warehouse1 = warehouse
                .stream()
                .filter(w -> w.getWarehousecode().equals(warehousecode))
                .findFirst()
                .orElseThrow();
        //2. Tạo shipperment gán orderid và washercode
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderid);
        shipment.setWarehouseName(warehouse1.getWarehouseName());
        shipment.setTrackingCode(genTracking());
        shipment.setStatus("WAIT_SHIPPING");
        shipment.setCreatedAt(LocalDateTime.now());
        OrderResponse order = orderRepository.getorderbyid(orderid);
        shipment.setEstimatedDeliveryTime(order.getExpecteddelivery());
        //Lưu shipment
        shipmentRepository.saveshipment(shipment);
        //Lưu order

        Order order1 = new Order();
        order1.setId(orderid);
        order1.setStatus(OrderStatus.PREPARING);
        //Lưu order
        orderRepository.save(order1);

        // Gán thông tin vào bảng statushistory
        OrderResponse order2 = orderRepository.getorderbyid(orderid);
        String title = "Chẩn bị hàng";

        orderstatusHistory.insertorderByStatus(orderid, order2.getOrderStatus(), title);
        return shipment;
    }

    @Override
    public Shipment assginShipper(String trackingcode) {
        // Lấy danh sách shiper AVAILABLE -> shiperment set tên shiper,
        // set thời gian dự kiến, set status DELIVERING
        //shiper set status thành BUSY
        //order set status thành DELIVERING
        Shipper shipper = shiperRepository.getlistShipper();
        Shipment shipment = shipmentRepository.getShipmentByTrackingcode(trackingcode);
        shipment.setShipperId(shipper.getId());
        shipment.setEstimatedDeliveryTime(getEstimatedDeliveryTime());
        shipment.setStatus("SHIPPING");
        shipper.setStatus("BUSY");
        shipmentRepository.saveshipment(shipment);
        shiperRepository.saveshiper(shipper);


        Order order2 = new Order();
        order2.setId(shipment.getOrderId());
        order2.setStatus(OrderStatus.SHIPPING);
        //Lưu order
        orderRepository.save(order2);

        // Gán thông tin vào bảng statushistory
        OrderResponse order = orderRepository.getorderbyid(shipment.getOrderId());
        String title = "Giao hàng cho shipper";
        orderstatusHistory.insertorderByStatus(shipment.getOrderId(), order.getOrderStatus(), title);
        return shipment;

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
