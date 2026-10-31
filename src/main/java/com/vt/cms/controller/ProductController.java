package com.vt.cms.controller;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import com.vt.cms.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/product")

public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    //Danh sách sp
    @GetMapping("listproduct")
    /**
     * Lấy danh sách list product. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public BaseResponse<PagingResponse<List<ProductResponse>>> listProduct(@ModelAttribute OrdersRequest request) {
        return productService.listproduct(request);
    }
    //Duyệt sản phẩm
    @PutMapping("approve/{product_id}")
    /**
     * Thực hiện xử lý nghiệp vụ approved product. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> approvedProduct(@PathVariable Integer product_id, HttpServletRequest request){
        log.info("Request approve product. productId={}", product_id);
        productService.editstatusproduct(product_id,
                (String) request.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE),
                (String) request.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE));
        return ResponseEntity.ok(new APIRessponse(200,"Duyệt sản phẩm thành công"));
    }
    // Từ chối sản phẩm đang chờ duyệt
    @PutMapping("reject/{product_id}")
    /**
     * Thực hiện xử lý nghiệp vụ reject product. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> rejectProduct(@PathVariable Integer product_id, HttpServletRequest request) {
        productService.rejectProduct(product_id, (String) request.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE));
        return ResponseEntity.ok(new APIRessponse(200, "Từ chối sản phẩm thành công"));
    }
    // Chi tiết sp
    @GetMapping("detail/{product_id}")
    /**
     * Thực hiện xử lý nghiệp vụ product detail. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> product_detail(@PathVariable Integer product_id) {
        ProductResponse response=  productService.detail(product_id);
       return ResponseEntity.ok(new APIRessponse(200,"Thành công", response));
    }

    // Thêm mới sản phẩm
    @PostMapping("addproduct")
    /**
     * Thêm dữ liệu addproduct. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> addproduct(  @Valid @RequestBody CreateProductRequest createProductRequest) {
        productService.addproduct(createProductRequest);
        return ResponseEntity.ok(new APIRessponse(200,"Thêm mới thành công"));
    }

    //Xóa sản phẩm
    @DeleteMapping("deleteproduct/{product_id}")
    /**
     * Xóa dữ liệu deleteproduct. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> deleteproduct(@PathVariable Integer product_id) {
        productService.deleteproduct(product_id);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa sản phẩm thành công"));
    }
    //Sửa sản phẩm ( còn hàm ửa chưa check)
    @PutMapping("editproduct/{id}")
    /**
     * Chỉnh sửa dữ liệu editproduct. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> editproduct(@PathVariable Integer id, @Valid @RequestBody CreateProductRequest createProductRequest) {
        log.info("Request update product. productId={}", id);
        productService.editproduct(id, createProductRequest);
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật sản phẩm thành công"));
    }
}
