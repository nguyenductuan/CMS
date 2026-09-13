package com.vt.cms.controller.Buyer;

import com.vt.cms.model.resp.ProductDetailResponse;
import com.vt.cms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController

public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product-detail")
    public ProductDetailResponse product_detail (@PathVariable Integer productId, Integer campainId){
      return  productService.product_detail(productId,campainId);
  }
}
