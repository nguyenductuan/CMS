package com.vt.cms.controller;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping

public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("listproduct")
    public BaseResponse<PagingResponse<List<ProductResponse>>> listProduct(@ModelAttribute OrdersRequest request) {
        return productService.listproduct(request);
    }

    @GetMapping("detail/{id}")
    public ProductResponse product_detail(@PathVariable Integer id) {
        return productService.detail(id);
    }

    @PostMapping("addproduct")
    public void addproduct(@RequestBody ProductRequest productRequest) {
        productService.addproduct(productRequest);

    }

    @PutMapping("editproduct/{id}")
    public void editproduct(@PathVariable Integer id, ProductRequest productRequest) {
        productService.editproduct(id, productRequest);
    }

    @DeleteMapping("deleteproduct")
    public void deleteproduct() {
    }

    @PutMapping("editstatus")
    public void editstatus() {
    }
}
