package com.vt.cms.controller;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/paymentorder")
    public void payment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.ProcessPayment(paymentRequest);
        // Hàm thanh toán
        // 1. Thanh toán thánh công -> cập nhật thông tin order vào bảng order với trạng thái Thnha toán thành công
        //                          -> cập nhật vào hàm writeLogOrder (gồm thông tin thời gian thanh toán đơn hàng thành công, người tác động( viết thêm 1 hàm actionlog lấy thông tin người tác động)
        // 2. Đơn chờ thanh toán cũng cập nhật như đơn thanh toán thành công
    }
}
