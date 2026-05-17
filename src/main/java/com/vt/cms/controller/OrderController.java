package com.vt.cms.controller;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("order-list")
    public BaseResponse<PagingResponse<List<OrderResponse>>> orderlist(
            @ModelAttribute OrdersRequest request) {
        return orderService.getorderlist(request);
    }

    @PostMapping("/order")
    public ResponseEntity<APIRessponse> order(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Order Created"));
    }


    @PostMapping("cancelorder/{orderId}")
    public ResponseEntity<APIRessponse> cancelorder(@PathVariable Integer orderId, String notecancel) {
        orderService.cancelOrder(orderId, notecancel);
        return ResponseEntity.ok(new APIRessponse(200, "Hủy đơn hàng thành công"));
    }

    @GetMapping("/order/{id}")
    public OrderResponse order(@PathVariable long id) {
        return orderService.getdetailorder(id);
    }

//    @GetMapping("/order/{customerId}")
//    public OrderResponse order(@PathVariable Integer customerId) {
//        return orderService.getlistorder();
//    }
}
