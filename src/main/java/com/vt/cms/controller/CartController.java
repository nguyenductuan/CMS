package com.vt.cms.controller;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addcart")
    /**
     * Thêm dữ liệu add to cart. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> addToCart(@Valid @RequestBody AddCartRequest request) {
        cartService.addToCart(request);
        return ResponseEntity.ok(
                new APIRessponse(200, "Thêm sản phẩm vào giỏ hàng thành công"));
    }

    @GetMapping("/list/{userId}")
    /**
     * Lấy danh sách list cart. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> listCart(
            @PathVariable("userId") @Positive(message = "User ID phải lớn hơn 0") int userId) {
        return ResponseEntity.ok(
                new APIRessponse(200, "Success", cartService.getCart(userId)));
    }

    @GetMapping("/count/{userId}")
    /**
     * Thực hiện xử lý nghiệp vụ count product cart. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> countProductCart(
            @PathVariable("userId") @Positive(message = "User ID phải lớn hơn 0") int userId) {
        return ResponseEntity.ok(
                new APIRessponse(200, "Success", cartService.getCartItemCount(userId)));
    }

    @DeleteMapping("/delete")
    /**
     * Xóa dữ liệu delete product and cart. Các tham số đầu vào và kết quả được xác định theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> deleteProductAndCart(
            @Valid @RequestBody DeleteRequest request) {
        int deletedCount = cartService.deleteproductAndCart(request);
        return ResponseEntity.ok(
                new APIRessponse(200, "Xóa sản phẩm khỏi giỏ hàng thành công", deletedCount));
    }
}
