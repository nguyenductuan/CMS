package com.vt.cms.controller;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.resp.APIRessponse;
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
    public List<OrderResponse> orderlist() {
        return orderService.getorderlist();
    }

    @PostMapping("/order")
    public ResponseEntity<APIRessponse> order(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Order Created"));
    }

//    @GetMapping("/order/{customerId}")
//    public OrderResponse order(@PathVariable Integer customerId) {
//        return orderService.getlistorder();
//    }

    @PostMapping("cancelorder/{orderId}")
    public ResponseEntity<APIRessponse> cancelorder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new APIRessponse(200, "Hủy đơn hàng thành công"));
    }

    @GetMapping("/order/{id}")
    public OrderResponse order(@PathVariable long id) {

        return orderService.getdetailorder(id);
    }


    //1. Cập nhật trạng thái order khi tạo mới: Done
    //2. Viết 1 API payment thanh toán giả lập: Done
    //3. Viết job cập nhật trạng thái đn hàng sau 1 ngày(viết bảng còfig thời gian này)
    //4. Viết API thêm mới sản phẩm
    //5. Cập nhật kho khi thanh toán than công
    //6. Cộng lại kho khi hủy đơn hàng
    //Viêt chức năng đánh giá sản phẩm
    //Viết chức năng like, coment
    //Viết API hành trình đơn hàng

}
