package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemRepository {

    int insertCartItem(CartItem cartItem);

    CartItem findBySkuAndCart(
            @Param("cartId") int cartId,
            @Param("skuId") int skuId);

    int updateCartItem(CartItem cartItem);

    List<CartItem> finByCartId(@Param("cartId") int cartId);

    int deleteProductCart(
            @Param("productIds") List<Integer> productIds,
            @Param("cartId") Integer cartId);

    int getCartItemCountById(@Param("cartId") int cartId);
}
