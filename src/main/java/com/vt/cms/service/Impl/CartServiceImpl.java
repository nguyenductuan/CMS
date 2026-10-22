package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.Cart;
import com.vt.cms.model.entity.CartItem;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.model.repository.CartItemRepository;
import com.vt.cms.model.repository.CartRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.repository.ProductSkuRepository;
import com.vt.cms.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            ProductSkuRepository productSkuRepository) {
        this.cartItemRepository = Objects.requireNonNull(cartItemRepository);
        this.cartRepository = Objects.requireNonNull(cartRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
        this.productSkuRepository = Objects.requireNonNull(productSkuRepository);
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

        Product_Sku sku = productSkuRepository.findNotDeletedById(request.getSkuId());
        if (sku == null || !Objects.equals(sku.getProduct_id(), request.getProductId())) {
            throw new IllegalArgumentException("SKU không tồn tại hoặc không thuộc sản phẩm đã chọn");
        }
        if (sku.getPrice() == null || sku.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Giá SKU không hợp lệ");
        }
        if (sku.getStock() == null || sku.getStock() < 0) {
            throw new IllegalStateException("Tồn kho SKU không hợp lệ");
        }
        if (request.getQuantity() > sku.getStock()) {
            throw new IllegalArgumentException("Số lượng yêu cầu vượt quá tồn kho SKU");
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

        CartItem existingItem = cartItemRepository.findBySkuAndCart(cart.getId(), sku.getId());
        if (existingItem == null) {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(request.getProductId());
            newItem.setSkuId(sku.getId());
            newItem.setQuantity(request.getQuantity());
            newItem.setProductName(product.getName());
            newItem.setProductPrice(sku.getPrice());

            if (cartItemRepository.insertCartItem(newItem) != 1) {
                throw new IllegalStateException("Không thể thêm SKU vào giỏ hàng");
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
        if (updatedQuantity > sku.getStock()) {
            throw new IllegalArgumentException("Tổng số lượng SKU trong giỏ vượt quá tồn kho");
        }

        existingItem.setQuantity(updatedQuantity);
        existingItem.setProductName(product.getName());
        existingItem.setProductPrice(sku.getPrice());
        if (cartItemRepository.updateCartItem(existingItem) != 1) {
            throw new IllegalStateException("Không thể cập nhật SKU trong giỏ hàng");
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
        if (request.getSkuId() == null || request.getSkuId() <= 0) {
            throw new IllegalArgumentException("SKU ID phải lớn hơn 0");
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
