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

    @Override
    @Transactional
    public List<CartItem> getCart(int userId) {
        Cart cart = cartRepository.finByUserId(userId);
        if (cart == null) {
            return List.of();
        }
        return cartItemRepository.finByCartId(cart.getId());
    }

    @Override
    @Transactional
    public int deleteproductAndCart(DeleteRequest request) {
        if (request == null || request.getProductids() == null || request.getProductids().isEmpty()) {
            return 0;
        }

        Cart cart = cartRepository.finByUserId(request.getUserid());
        if (cart == null) {
            return 0;
        }
        return cartItemRepository.deleteProductCart(request.getProductids(), cart.getId());
    }

    @Override
    @Transactional
    public int getCartItemCount(int userId) {
        Cart cart = cartRepository.finByUserId(userId);
        if (cart == null) {
            return 0;
        }
        return cartItemRepository.getCartItemCountById(cart.getId());
    }

    @Override
    @Transactional
    public void addToCart(AddCartRequest request) {
        validateAddToCart(request);

        ProductResponse product = productRepository.detailProduct(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }

        Cart cart = cartRepository.finByUserId(request.getUserId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cartRepository.insertCart(cart);
            // The repository must populate the generated ID before this service continues.
            if (cart.getId() == null) {
                throw new IllegalStateException("Không thể tạo giỏ hàng");
            }
        }

        CartItem existingItem = cartItemRepository.findByProductIDAndCart(
                cart.getId(),
                request.getProductId());

        int existingQuantity = existingItem == null ? 0 : existingItem.getQuantity();
        validateQuantity(existingQuantity, request.getQuantity(), product.getStock());

        if (existingItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItemRepository.insertCartItem(cartItem);
            return;
        }

        existingItem.setQuantity(existingQuantity + request.getQuantity());
        existingItem.setProductName(product.getName());
        existingItem.setProductPrice(product.getPrice());
        cartItemRepository.updateCartItem(existingItem);
    }

    private void validateQuantity(int existingQuantity, int requestedQuantity, Integer stock) {
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0");
        }
        if (stock == null || existingQuantity + requestedQuantity > stock) {
            throw new IllegalArgumentException(
                    "Số lượng sản phẩm trong giỏ hàng không được vượt quá số lượng tồn kho");
        }
    }

    private void validateAddToCart(AddCartRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống");
        }
        if (request.getUserId() <= 0 || request.getProductId() <= 0) {
            throw new IllegalArgumentException("User ID và Product ID phải hợp lệ");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0");
        }
    }
}
