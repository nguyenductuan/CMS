package com.vt.cms.service.Impl;

import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.Shipment;
import com.vt.cms.model.entity.Shipper;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.*;
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
    ShiperRepository shiperRepository;

    public DeliveryServiceImpl(WarehouseRepository warehouseRepository,
                               OrderRepository orderRepository,
                               ShippingRepository shippingRepository,
                               ShipmentRepository shipmentRepository,
                               ShiperRepository shiperRepository) {
        this.warehouseRepository = warehouseRepository;
        this.orderRepository = orderRepository;
        this.shippingRepository = shippingRepository;
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
        shipment.setStatus("READY");
        shipment.setCreatedAt(LocalDateTime.now());
        //Lưu shipment
        shipmentRepository.saveshipment(shipment);
        //Lưu order
        Order order = orderRepository.getdetailById(orderid);
        order.setStatus(OrderStatus.PREPARING);
        //Lưu order
        orderRepository.save(order);
        return shipment;//response trả ra thông tin trackingcode
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
        shipment.setEstimatedDeliveryTime(getEstimatedDeliveryTime());// lỗi không lấy được thời gian giao dự kiến
        shipment.setStatus("DELIVERING");
        shipper.setStatus("BUSY");
        shipmentRepository.saveshipment(shipment);
        shiperRepository.saveshiper(shipper);

        Order order = orderRepository.getdetailById(shipment.getOrderId());
        order.setStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);
        return shipment;
    }

    @Override
    public void shipeperdelivery(int orderid) {

    }

    //hàm sinh mã vâ đơn
    public String genTracking() {
        return "VTP" + System.currentTimeMillis();
    }

    //Hàm tính thời gian giao d kiến
    public LocalDateTime getEstimatedDeliveryTime() {
        return LocalDateTime.now().plusDays(2);
    }
}
