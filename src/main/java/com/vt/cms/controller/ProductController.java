package com.vt.cms.controller;

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

    @GetMapping("listproduct")
    public void listProduct() {
        productService.listproduct();

    }

    @GetMapping("detail/{id}")
    public void detail(@PathVariable int id) {
        productService.detail(id);

    }
}
