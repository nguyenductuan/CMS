package com.vt.cms.product.api;

import com.vt.cms.product.application.dto.response.ProductDetailResponse;
import com.vt.cms.product.application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("b/v1/products")
public class ProductBuyerController {
  @Autowired private final ProductService productService;

  public ProductBuyerController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/product-detail/{product_id}/{campaign_id}")
  public ProductDetailResponse productDetail(
      @PathVariable Integer product_id, @PathVariable Integer campaign_id) {
    return productService.product_detail(product_id, campaign_id);
  }
}
