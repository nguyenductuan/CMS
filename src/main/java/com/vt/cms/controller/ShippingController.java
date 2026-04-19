package com.vt.cms.controller;

import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ShippingResponse;
import com.vt.cms.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ShippingController {
    @Autowired

    private ShippingService shippingService;

    @GetMapping("list_shipping")
    public BaseResponse<?> list_shipping() {
        return BaseResponse.of(shippingService.getShipping());
    }

    @GetMapping("/shipping/{id}")
    public BaseResponse<ShippingResponse> getdetailshipping(@PathVariable String id) {
        return BaseResponse.of(shippingService.getDetailshipping(id));
    }
}
