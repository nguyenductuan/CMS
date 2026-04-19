package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartItemRepository {
    int insertCartItem(CartItem cartItem);

    CartItem findByProductIDAndCart(int cartId, int productId);

    void updateCartItem(CartItem cartItem);

    List<CartItem> finByCartId(int cartId);

    int deleteproductCart(int productId, int cartId);
}
