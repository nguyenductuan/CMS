package com.vt.cms.controller;

import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping

public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("detail/{id}")
    public BaseResponse<ProductResponse> detail(@PathVariable int id) {
        return BaseResponse.of(productService.detail(id));
    }
}
