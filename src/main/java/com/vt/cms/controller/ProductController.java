package com.vt.cms.controller;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v/product")

public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    //Danh sách sp
    @GetMapping("listproduct")
    public BaseResponse<PagingResponse<List<ProductResponse>>> listProduct(@ModelAttribute OrdersRequest request) {
        return productService.listproduct(request);
    }
    //Duyệt sản phẩm
    @PutMapping("approve/{product_id}")
    public ResponseEntity<APIRessponse> approvedProduct(@PathVariable Integer product_id){
        log.info("Request approve product. productId={}", product_id);
        productService.editstatusproduct(product_id);
        return ResponseEntity.ok(new APIRessponse(200,"Duyệt sản phẩm thành công"));
    }
    // Từ chối sản phẩm đang chờ duyệt
    @PutMapping("reject/{product_id}")
    public ResponseEntity<APIRessponse> rejectProduct(@PathVariable Integer product_id) {
        productService.rejectProduct(product_id);
        return ResponseEntity.ok(new APIRessponse(200, "Từ chối sản phẩm thành công"));
    }
    // Chi tiết sp
    @GetMapping("detail/{product_id}")
    public ResponseEntity<APIRessponse> product_detail(@PathVariable Integer product_id) {
        ProductResponse response=  productService.detail(product_id);
       return ResponseEntity.ok(new APIRessponse(200,"Thành công", response));
    }
    // Chi tiết sản phẩm trong chiến dịch
    @GetMapping("campaign/{campaign_id}/product/{product_id}")
    public ResponseEntity<APIRessponse> campaignProductDetail(
            @PathVariable Integer campaign_id, @PathVariable Integer product_id) {
        var response = productService.product_detail(campaign_id, product_id);
        if (response == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new APIRessponse(200, "Lấy chi tiết sản phẩm trong chiến dịch thành công", response));
    }
    // Thêm mới sản phẩm
    @PostMapping("addproduct")
    public ResponseEntity<APIRessponse> addproduct(  @Valid @RequestBody CreateProductRequest createProductRequest) {
        productService.addproduct(createProductRequest);
        return ResponseEntity.ok(new APIRessponse(200,"Thêm mới thành công"));
    }
    //Sửa sản phẩm
    @PutMapping("editproduct/{id}")
    public ResponseEntity<APIRessponse> editproduct(@PathVariable Integer id, @Valid @RequestBody CreateProductRequest createProductRequest) {
        log.info("Request update product. productId={}", id);
        productService.editproduct(id, createProductRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật sản phẩm thành công"));
    }
    //Xóa sản phẩm
    @DeleteMapping("deleteproduct/{product_id}")
    public ResponseEntity<APIRessponse> deleteproduct(@PathVariable Integer product_id) {
        productService.deleteproduct(product_id);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa sản phẩm thành công"));
    }
}
