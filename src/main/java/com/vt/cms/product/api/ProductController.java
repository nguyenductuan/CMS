package com.vt.cms.product.api;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.product.application.dto.req.CreateProductRequest;
import com.vt.cms.product.application.dto.response.ProductResponse;
import com.vt.cms.product.application.service.ProductService;
import com.vt.cms.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/product")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  // Danh sách sp
  @GetMapping("listproduct")
  public BaseResponse<PagingResponse<List<ProductResponse>>> listProduct(
      @ModelAttribute Pagerequest request) {
    return productService.listproduct(request);
  }

  // Duyệt sản phẩm
  @PutMapping("approve/{product_id}")
  public ResponseEntity<APIRessponse> approvedProduct(
      @PathVariable Integer product_id, HttpServletRequest httpRequest) {
    log.info("Request approve product. productId={}", product_id);
    Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    productService.approvedProduct(product_id, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Duyệt sản phẩm thành công"));
  }

  // Từ chối sản phẩm đang chờ duyệt
  @PutMapping("reject/{product_id}")
  public ResponseEntity<APIRessponse> rejectProduct(
      @PathVariable Integer product_id, HttpServletRequest request, String reason) {
    Integer userId = (Integer) request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);

    productService.rejectProduct(product_id, userId, reason);
    return ResponseEntity.ok(new APIRessponse(200, "Từ chối sản phẩm thành công"));
  }

  // Chi tiết sp
  @GetMapping("detail/{product_id}")
  public ResponseEntity<APIRessponse> product_detail(@PathVariable Integer product_id) {
    ProductResponse response = productService.detail(product_id);
    return ResponseEntity.ok(new APIRessponse(200, "Thành công", response));
  }

  // Thêm mới sản phẩm
  @PostMapping("addProduct")
  public ResponseEntity<APIRessponse> addProduct(
      @Valid @RequestBody CreateProductRequest createProductRequest,
      HttpServletRequest httpRequest) {
    // Lấy thông tin người đăng nhập từ JWT Filter
    Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    productService.addProduct(createProductRequest, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Thêm mới thành công"));
  }

  // Xóa sản phẩm
  @DeleteMapping("deleteProduct/{product_id}")
  public ResponseEntity<APIRessponse> deleteProduct(@PathVariable Integer product_id) {
    log.info("Request delete product. productId={}", product_id);
    productService.deleteProduct(product_id);
    return ResponseEntity.ok(new APIRessponse(200, "Xóa sản phẩm thành công"));
  }

  // Sửa sản phẩm
  @PutMapping("editProduct/{id}")
  public ResponseEntity<APIRessponse> editProduct(
      @PathVariable Integer id,
      @Valid @RequestBody CreateProductRequest createProductRequest,
      HttpServletRequest httpServletRequest) {
    log.info("Request update product. productId={}", id);
    Integer userId =
        (Integer) httpServletRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    productService.editproduct(id, createProductRequest, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Cập nhật sản phẩm thành công"));
  }
}
