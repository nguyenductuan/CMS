package com.vt.cms.controller;

import com.vt.cms.service.DeliveryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    //1. Gán kho hàng -> cập nhật trạng thái thành Đã chuẩn bị hàng
    @PostMapping("prepare/{orderid}")
    public void prepare(@PathVariable Integer orderid, Integer warehousecode) {
        deliveryService.prepare(orderid, warehousecode);
    }

    //2.  Asgin cho shipper
    @PostMapping("assgin/{trackingcode}")
    public void asginShipper(@PathVariable String trackingcode) {
        deliveryService.assginShipper(trackingcode);
    }

    //4. Shipper giao xong
    @PostMapping("shipper_done/{orderid}")
    public void shipperDone(@PathVariable Integer orderid) {
        deliveryService.shipeperdelivery(orderid);
    }
}
