package com.vt.cms.controller;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v/product")

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
    @PutMapping("approve/{product_id}")
    //Duyệt sản phẩm
    public ResponseEntity<APIRessponse> approvedProduct(@PathVariable Integer product_id){
        log.info("Duyệt sản phẩm:", product_id);
        productService.editstatusproduct(product_id);
        return ResponseEntity.ok(new APIRessponse(200,"Duyệt sản phẩm thành công"));
    }
    @GetMapping("detail/{id}")
    public ProductResponse product_detail(@PathVariable Integer id) {
        return productService.detail(id);
    }
    @PostMapping("addproduct")
    // Thêm mới sản phẩm
    public ResponseEntity<APIRessponse> addproduct(  @Valid @RequestBody ProductRequest productRequest) {
        productService.addproduct(productRequest);
        return ResponseEntity.ok(new APIRessponse(200,"Thêm mới thành công"));
    }
    @PutMapping("editproduct/{id}")
    //Sửa sản phẩm
    public void editproduct(@PathVariable Integer id, ProductRequest productRequest) {
        productService.editproduct(id, productRequest);
    }
    @DeleteMapping("deleteproduct/{id}")
    //Xóa sản phẩm
    public void deleteproduct(@PathVariable Integer id) {
        productService.deleteproduct(id);
    }
}
