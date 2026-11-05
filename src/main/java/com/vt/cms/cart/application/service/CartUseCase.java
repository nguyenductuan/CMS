package com.vt.cms.cart.application.service;
import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.dto.DeleteRequest;
import com.vt.cms.model.entity.CartItem;
import java.util.List;
/** Application boundary for cart commands and queries. */
public interface CartUseCase {
 void addToCart(AddCartRequest request);
 List<CartItem> getCart(int userId);
 int getCartItemCount(int userId);
 int deleteproductAndCart(DeleteRequest request);
}