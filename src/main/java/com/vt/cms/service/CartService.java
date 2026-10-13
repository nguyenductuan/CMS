package com.vt.cms.service;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.CartItem;

import java.util.List;

public interface CartService {
    void addToCart(AddCartRequest request);

    List<CartItem> getCart(int userId);

    int getCartItemCount(int userId);

    int deleteproductAndCart(DeleteRequest request);
}
