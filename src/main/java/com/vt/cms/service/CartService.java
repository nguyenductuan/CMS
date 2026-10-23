package com.vt.cms.service;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.CartItem;

import java.util.List;

public interface CartService {
    /**
     * Thêm add to cart. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void addToCart(AddCartRequest request);

    /**
     * Lấy dữ liệu get cart. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<CartItem> getCart(int userId);

    /**
     * Lấy dữ liệu get cart item count. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    int getCartItemCount(int userId);

    /**
     * Xóa deleteproduct and cart. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    int deleteproductAndCart(DeleteRequest request);
}
