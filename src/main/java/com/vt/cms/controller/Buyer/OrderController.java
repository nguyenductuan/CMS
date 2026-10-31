package com.vt.cms.controller.Buyer;

import com.vt.cms.model.dto.OrderRequest;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.enums.Order;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.OrderCreateResponse;
import com.vt.cms.model.resp.OrderResponse;
import com.vt.cms.service.OrderService;
import com.vt.cms.service.OrderHistoryService;
import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;

    public OrderController(OrderService orderService, OrderHistoryService orderHistoryService) {
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
    }
    //Hàm lấy danh sách order
    @GetMapping("order-list")
    /**
     * Xử lý thông tin đơn hàng orderlist; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public BaseResponse<PagingResponse<List<OrderResponse>>> orderlist(@ModelAttribute OrdersRequest request) {
        return orderService.getorderlist(request);
    }
    // Ham tao order
    @PostMapping("/order")
    /**
     * Xử lý thông tin đơn hàng order; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse>  order(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        OrderCreateResponse orderCreateResponse = orderService.createOrder(orderRequest,
                (String) request.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE));
        return ResponseEntity.ok(new APIRessponse(200, Order.ORDER_SUCCESS.getMessage(), orderCreateResponse));

    }
    // Ham huy order
    @PostMapping("cancelorder/{orderId}")
    /**
     * Hủy nghiệp vụ cancelorder; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse> cancelorder(@PathVariable Integer orderId,
            @RequestParam(required = false) String notecancel, HttpServletRequest request) {
        orderService.cancelOrder(orderId, notecancel,
                (String) request.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) request.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE));
        return ResponseEntity.ok(new APIRessponse(200, Order.ORDER_CANCEL.getMessage()));
    }
    /** Lấy lịch sử tác động đơn hàng theo thứ tự thời gian tăng dần. */
    @GetMapping("/order/{id}/history")
    public ResponseEntity<List<OrderTracking>> orderHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(orderHistoryService.getHistory(id));
    }

    // Hàm chi tiết order
    @GetMapping("/order/{id}")
    /**
     * Xử lý thông tin đơn hàng order; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public OrderResponse order(@PathVariable long id) {
        return orderService.getdetailorder(id);
    }
}
