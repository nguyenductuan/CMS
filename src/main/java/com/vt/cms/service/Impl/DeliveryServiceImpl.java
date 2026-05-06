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
        //Lưu shipment
        shipmentRepository.saveshipment(shipment);
        //Lưu order

        Order order1 = new Order();
        order1.setId(orderid);
        order1.setStatus(OrderStatus.PREPARING);
        //Lưu order
        orderRepository.save(order1);

        // Gán thông tin vào bảng statushistory
        OrderResponse order = orderRepository.getorderbyid(orderid);
        orderstatusHistory.insertorderByStatus(orderid, order.getOrderStatus());
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
        shipment.setStatus("DELIVERING");
        shipper.setStatus("BUSY");
        shipmentRepository.saveshipment(shipment);
        shiperRepository.saveshiper(shipper);

        //OrderResponse order = orderRepository.getdetailById(shipment.getOrderId());
        //order.setStatus(OrderStatus.SHIPPING);
        // orderRepository.save(order);
        return shipment;
    }

    @Override
    public void shipeperdelivery(int orderid) {
        //Không cập nhật trạng thái đơn hàng cần user bấm xác nhận
        System.out.println("Shipper delivered but waiting user confirm");
    }

    @Override
    public OrderResponse confirmReceived(int orderid) {
        //Set status DELIVERED, set thời gian nhận hàng
        OrderResponse order = orderRepository.getorderbyid(orderid);
        //OrderResponse order = orderRepository.getdetailById(orderid);
        // order.setStatus(OrderStatus.DELIVERED);
        // order.setDeliveredAt(LocalDateTime.now());
//       / orderRepository.save(order);
        return order;
    }

    //hàm sinh mã vâ đơn
    public String genTracking() {
        return "VTP" + System.currentTimeMillis();
    }

    //Hàm tính thời gian giao dự kiến
    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2);
    }
}
