package com.vt.cms.service.Impl;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
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
import org.springframework.dao.DuplicateKeyException;
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

    /**
     * Thực hiện xử lý nghiệp vụ cart service impl. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
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
    /**
     * Lấy dữ liệu get cart. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
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
    /**
     * Xóa dữ liệu deleteproduct and cart. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public int deleteproductAndCart(DeleteRequest request) {
        if (request == null) {
            throw new BusinessException(MessageCode.YEU_CAU_XOA_GIO_HANG_KHONG_DUOC_DE_TRONG);
        }
        validateUserId(request.getUserid());

        List<Integer> productIds = request.getProductids();
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException(MessageCode.DANH_SACH_PRODUCT_ID_KHONG_DUOC_DE_TRONG);
        }
        if (productIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new BusinessException(MessageCode.TAT_CA_PRODUCT_ID_PHAI_LON_HON_0);
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
    /**
     * Lấy dữ liệu get cart item count. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
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
    /**
     * Thêm dữ liệu add to cart. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void addToCart(AddCartRequest request) {
        validateAddToCart(request);

        ProductResponse product = productRepository.detailProduct(request.getProductId());
        if (product == null) {
            throw new BusinessException(MessageCode.SAN_PHAM_KHONG_TON_TAI);
        }

        Product_Sku sku = productSkuRepository.findNotDeletedById(request.getSkuId());
        if (sku == null || !Objects.equals(sku.getProduct_id(), request.getProductId())) {
            throw new BusinessException(MessageCode.SKU_KHONG_TON_TAI_HOAC_KHONG_THUOC_SAN_PHAM_DA_C);
        }
        if (sku.getPrice() == null || sku.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Giá SKU không hợp lệ");
        }
        if (sku.getStock() == null || sku.getStock() < 0) {
            throw new IllegalStateException("Tồn kho SKU không hợp lệ");
        }
        if (request.getQuantity() > sku.getStock()) {
            throw new BusinessException(MessageCode.SO_LUONG_YEU_CAU_VUOT_QUA_TON_KHO_SKU);
        }

        Cart cart = cartRepository.finByUserId(request.getUserId());
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUserId(request.getUserId());
            try {
                int insertedRows = cartRepository.insertCart(newCart);
                if (insertedRows == 1) {
                    cart = newCart;
                }
            } catch (DuplicateKeyException concurrentCreate) {
                // Another request created the user's cart after our initial lookup.
                cart = cartRepository.finByUserId(request.getUserId());
            }
            if (cart == null || cart.getId() == null) {
                throw new IllegalStateException("Không thể tạo hoặc tải giỏ hàng");
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

            try {
                if (cartItemRepository.insertCartItem(newItem) == 1) {
                    return;
                }
            } catch (DuplicateKeyException concurrentInsert) {
                // Another request inserted this SKU concurrently; re-read and update it.
            }
            existingItem = cartItemRepository.findBySkuAndCart(cart.getId(), sku.getId());
            if (existingItem == null) {
                throw new IllegalStateException("Không thể thêm SKU vào giỏ hàng");
            }
        }

        int currentQuantity = existingItem.getQuantity() == null ? 0 : existingItem.getQuantity();
        if (currentQuantity < 0) {
            throw new IllegalStateException("Số lượng sản phẩm hiện tại không hợp lệ");
        }

        final int updatedQuantity;
        try {
            updatedQuantity = Math.addExact(currentQuantity, request.getQuantity());
        } catch (ArithmeticException ex) {
            throw new BusinessException(MessageCode.SO_LUONG_SAN_PHAM_VUOT_GIOI_HAN_CHO_PHEP, ex);
        }
        if (updatedQuantity > sku.getStock()) {
            throw new BusinessException(MessageCode.TONG_SO_LUONG_SKU_TRONG_GIO_VUOT_QUA_TON_KHO);
        }

        existingItem.setQuantity(updatedQuantity);
        existingItem.setProductName(product.getName());
        existingItem.setProductPrice(sku.getPrice());
        if (cartItemRepository.updateCartItem(existingItem) != 1) {
            throw new IllegalStateException("Không thể cập nhật SKU trong giỏ hàng");
        }
    }

    /**
     * Kiểm tra tính hợp lệ validate add to cart. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateAddToCart(AddCartRequest request) {
        if (request == null) {
            throw new BusinessException(MessageCode.YEU_CAU_KHONG_DUOC_DE_TRONG);
        }
        validateUserId(request.getUserId());
        if (request.getProductId() == null || request.getProductId() <= 0) {
            throw new BusinessException(MessageCode.PRODUCT_ID_PHAI_LON_HON_0);
        }
        if (request.getSkuId() == null || request.getSkuId() <= 0) {
            throw new BusinessException(MessageCode.SKU_ID_PHAI_LON_HON_0);
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BusinessException(MessageCode.SO_LUONG_SAN_PHAM_PHAI_LON_HON_0);
        }
    }

    /**
     * Kiểm tra tính hợp lệ validate user id. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(MessageCode.USER_ID_PHAI_LON_HON_0);
        }
    }
}
