package com.vt.cms.controller;

import com.vt.cms.model.dto.OrderShippingRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.OrderShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class OrderShippingController {
    private final OrderShippingService orderShippingService;

    public OrderShippingController(OrderShippingService orderShippingService) {
        this.orderShippingService = orderShippingService;
    }

    //1. Gán kho hàng -> cập nhật trạng thái thành Đã chuẩn bị hàng
    @PostMapping("prepare")
    public ResponseEntity<APIRessponse> prepareOrder(@RequestBody OrderShippingRequest orderShippingRequest) {
        orderShippingService.prepare(orderShippingRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Đã chuẩn bị hàng thành công"));
    }

    //2.  Asgin cho shipper
    @PostMapping("assgin/{trackingcode}")
    public void asginShipper(@PathVariable String trackingcode) {
        orderShippingService.assginShipper(trackingcode);
    }
    //4. Shipper giao xong
    @PostMapping("shipper_done")
    public void shipperDone(@RequestParam Integer orderid) {
        orderShippingService.shipeperdelivery(orderid);
    }
    @PostMapping("/confirm")
    public void userconfirm(int orderid) {
        orderShippingService.confirmReceived(orderid);
    }
}
