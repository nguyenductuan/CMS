package com.vt.cms.service;

import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.CartItem;

import java.util.List;

public interface CartService {
    void addcart(AddCartRequest request);

    List<CartItem> getcart(int userId);

    int CountCartById(int userId);

    int deleteproductAndCart(DeleteRequest request);
}
