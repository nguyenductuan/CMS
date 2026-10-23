package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemRepository {

    /**
     * Thêm bản ghi insert cart item. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int insertCartItem(CartItem cartItem);

    /**
     * Tìm kiếm find by sku and cart. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    CartItem findBySkuAndCart(
            @Param("cartId") int cartId,
            @Param("skuId") int skuId);

    /**
     * Cập nhật update cart item. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int updateCartItem(CartItem cartItem);

    /**
     * Thực hiện truy cập dữ liệu fin by cart id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<CartItem> finByCartId(@Param("cartId") int cartId);

    /**
     * Xóa delete product cart. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int deleteProductCart(
            @Param("productIds") List<Integer> productIds,
            @Param("cartId") Integer cartId);

    /**
     * Lấy dữ liệu get cart item count by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int getCartItemCountById(@Param("cartId") int cartId);
}
