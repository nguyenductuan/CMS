package com.vt.cms.controller;

import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    //1. Gán kho hàng -> cập nhật trạng thái thành Đã chuẩn bị hàng
    @PostMapping("prepare/{orderid}")
    public ResponseEntity<APIRessponse> prepare(@PathVariable Integer orderid, Integer warehousecode) {
        deliveryService.prepare(orderid, warehousecode);
        return ResponseEntity.ok(
                new APIRessponse(200, "Thành công", errors)
        );
    }

    //2.  Asgin cho shipper
    @PostMapping("assgin/{trackingcode}")
    public void asginShipper(@PathVariable String trackingcode) {
        deliveryService.assginShipper(trackingcode);
    }

    //4. Shipper giao xong
    @PostMapping("shipper_done")
    public void shipperDone(@RequestParam Integer orderid) {

        deliveryService.shipeperdelivery(orderid);
    }

    @PostMapping("/confirm")
    public void userconfirm(int orderid) {
        deliveryService.confirmReceived(orderid);
    }
}
