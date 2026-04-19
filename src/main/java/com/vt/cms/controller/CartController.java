package com.vt.cms.controller;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.CartItem;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //Thêm vào cart
    @PostMapping("/addcart")
    public ResponseEntity<APIRessponse> AddtoCart(@RequestBody AddCartRequest request) {
        cartService.addcart(request);
        return ResponseEntity.ok(
                new APIRessponse(200, "Thành công")
        );
    }

    //    Lấy danh sách item trong cart
    @GetMapping("list/{userId}")
    public BaseResponse<List<CartItem>> ListcartController(@PathVariable("userId") int userId) {

        return BaseResponse.of(cartService.getcart(userId));
    }

    //    Lâý tổng số lợng sản phẩm trong Cart
    @GetMapping("count/{userId}")
    public BaseResponse<Integer> Countcart(@PathVariable("userId") int userId) {
        return BaseResponse.of(cartService.CountCartById(userId));
    }

    //    Xóa sp trong cart
    @DeleteMapping("delete")
    public ResponseEntity<APIRessponse> DeleteProductAndCart(@RequestBody DeleteRequest request) {
        cartService.deleteproductAndCart(request);

        return ResponseEntity.ok(
                new APIRessponse(200, "Xóa sản phẩm không giỏ hàng thành công")
        );
    }
}
