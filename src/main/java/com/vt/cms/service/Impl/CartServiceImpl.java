package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.Cart;
import com.vt.cms.model.entity.CartItem;
import com.vt.cms.model.repository.CartItemRepository;
import com.vt.cms.model.repository.CartRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.CartService;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public List<CartItem> getCart(int userId) {
        Cart cart = cartRepository.finByUserId(userId);
        if (cart == null) {
            return List.of();
        }
        return cartItemRepository.finByCartId(cart.getId());
    }
    @Transactional
    public int deleteproductAndCart(DeleteRequest request) {

        if (request == null || request.getProductids() == null || request.getProductids().isEmpty()) {
           return  0;
        }
        Cart cart = cartRepository.finByUserId(request.getUserid());
        if (cart == null) {
            return 0;
        }
        return cartItemRepository.deleteProductCart(request.getProductids(), cart.getId());
    }
    @Transactional
    public int getCartItemCount(int userId) {
        Cart cart = cartRepository.finByUserId(userId);
        return cartItemRepository.getCartItemCountById(cart.getId());
    }

    @Transactional
    public void addToCart(AddCartRequest request) {
        validateaddtocart(request);
        //1. lấy thông tin cart theo userId nếu chưa có thì tạo mới
        ProductResponse product = productRepository.detailProduct(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        Cart cart = cartRepository.finByUserId(request.getUserId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cartRepository.insertCart(cart);
        }
//        2. Nếu đã có cart check xem có sản phẩm trong giỏ không nếu không có thì thêm mới vào cart nếu có thì
//        cập nhật số lượng
        CartItem exitcartItem = cartItemRepository.findByProductIDAndCart(
                cart.getId(),
                request.getProductId()
        );
        if (exitcartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(request.getProductId());
            ValidateQuantity(0, request.getQuantity(), product.getStock());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItemRepository.insertCartItem(cartItem);
        } else {
            ValidateQuantity(exitcartItem.getQuantity(), request.getQuantity(), product.getStock());
            exitcartItem.setQuantity(exitcartItem.getQuantity() + request.getQuantity());
            cartItemRepository.updateCartItem(exitcartItem);
        }
    }
    private void ValidateQuantity(int existingQuantity, int requestedQuantity, int stock) {
        int newQuantity = existingQuantity + requestedQuantity;
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0");
        }
        if (newQuantity > stock) {
            throw new IllegalArgumentException("Số lượng sản phẩm trong giỏ hàng không được vượt quá số lượng tồn kho");
        }
    }
    private void validateaddtocart(AddCartRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống");
        }

    }
}
