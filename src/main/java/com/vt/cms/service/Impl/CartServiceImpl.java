package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.Cart;
import com.vt.cms.model.entity.CartItem;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.model.repository.CartItemRepository;
import com.vt.cms.model.repository.CartRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.cartItemRepository = Objects.requireNonNull(cartItemRepository);
        this.cartRepository = Objects.requireNonNull(cartRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCart(int userId) {
        validateUserId(userId);
        Cart cart = cartRepository.finByUserId(userId);
        if (cart == null || cart.getId() == null) {
            return List.of();
        }

        List<CartItem> items = cartItemRepository.finByCartId(cart.getId());
        return items == null ? List.of() : List.copyOf(items);
    }

    @Override
    @Transactional
    public int deleteproductAndCart(DeleteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu xóa giỏ hàng không được để trống");
        }
        validateUserId(request.getUserid());

        List<Integer> productIds = request.getProductids();
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách product ID không được để trống");
        }
        if (productIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new IllegalArgumentException("Tất cả product ID phải lớn hơn 0");
        }

        Cart cart = cartRepository.finByUserId(request.getUserid());
        if (cart == null || cart.getId() == null) {
            return 0;
        }

        return cartItemRepository.deleteProductCart(
                productIds.stream().distinct().toList(), cart.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCartItemCount(int userId) {
        validateUserId(userId);
        Cart cart = cartRepository.finByUserId(userId);
        if (cart == null || cart.getId() == null) {
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

            int insertedRows = cartRepository.insertCart(cart);
            if (insertedRows != 1 || cart.getId() == null) {
                throw new IllegalStateException("Không thể tạo giỏ hàng");
            }
        }
        if (cart.getId() == null) {
            throw new IllegalStateException("Giỏ hàng không có ID hợp lệ");
        }

        CartItem existingItem = cartItemRepository.findByProductIDAndCart(
                cart.getId(), request.getProductId());

        if (existingItem == null) {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setProductName(product.getName());

            if (cartItemRepository.insertCartItem(newItem) != 1) {
                throw new IllegalStateException("Không thể thêm sản phẩm vào giỏ hàng");
            }
            return;
        }

        int currentQuantity = existingItem.getQuantity() == null ? 0 : existingItem.getQuantity();
        if (currentQuantity < 0) {
            throw new IllegalStateException("Số lượng sản phẩm hiện tại không hợp lệ");
        }

        final int updatedQuantity;
        try {
            updatedQuantity = Math.addExact(currentQuantity, request.getQuantity());
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Số lượng sản phẩm vượt giới hạn cho phép", ex);
        }

        existingItem.setQuantity(updatedQuantity);
        existingItem.setProductName(product.getName());
        if (cartItemRepository.updateCartItem(existingItem) != 1) {
            throw new IllegalStateException("Không thể cập nhật sản phẩm trong giỏ hàng");
        }
    }

    private void validateAddToCart(AddCartRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống");
        }
        validateUserId(request.getUserId());

        if (request.getProductId() == null || request.getProductId() <= 0) {
            throw new IllegalArgumentException("Product ID phải lớn hơn 0");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0");
        }
    }

    private void validateUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID phải lớn hơn 0");
        }
    }
}
