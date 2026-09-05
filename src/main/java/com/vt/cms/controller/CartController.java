package com.vt.cms.controller;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //Thêm vào cart
    @PostMapping("/addcart")
    public ResponseEntity<APIRessponse> addToCart( @Valid @RequestBody AddCartRequest request) {
        cartService.addToCart(request);
        return ResponseEntity.ok(new APIRessponse(200, "Thêm sản phẩm vào giỏ hàng thành công")
        );
    }
    //    Lấy danh sách item trong cart
    @GetMapping("list/{userId}")
    public ResponseEntity<APIRessponse>listCart(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(new APIRessponse(200, "Success", cartService.getCart(userId)));
    }
    //    Lâý tổng số lợng sản phẩm trong Cart
    @GetMapping("count/{userId}")
    public ResponseEntity<APIRessponse> countProductCart(@PathVariable("userId") int userId) {
        int count =  cartService.getCartItemCount(userId);
        return ResponseEntity.ok(new APIRessponse(200, "Success", count));
    }
    //    Xóa sp trong cart
    @DeleteMapping("delete")
    public ResponseEntity<APIRessponse> DeleteProductAndCart( @Valid @RequestBody DeleteRequest request) {
        cartService.deleteproductAndCart(request);
        return ResponseEntity.ok(
                new APIRessponse(200, "Xóa sản phẩm không giỏ hàng thành công")
        );
    }
}
