package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.AddCartRequest;
import com.vt.cms.model.entity.Cart;
import com.vt.cms.model.entity.CartItem;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.repository.CartItemRepository;
import com.vt.cms.model.repository.CartRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.service.CartService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getcart(int userId) {
        Cart cart = cartRepository.finByUserId(userId);
        return cartItemRepository.finByCartId(cart.getId());
    }

    public void deleteproductAndCart(int productId, int userId) {
        List<CartItem> cartItems = getcart(userId);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductId() == productId) {
                cartItemRepository.deleteproductCart(productId, cartItem.getCartId());
            }
        }
    }

    public int CountCartById(int userId) {
        List<CartItem> cartItems = getcart(userId);
        int coutproduct = 0;
        for (CartItem items : cartItems) {
            coutproduct += items.getQuantity();
        }
        return coutproduct;
    }

    public void addcart(AddCartRequest request) {
//        1. Lấy thông tin cart theo userid nếu chưa có tạo mới cart
//        1.1. Check xem cart theo userid đã cso chưa
        Cart cart = cartRepository.finByUserId(request.getUserId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cartRepository.insertCart(cart);
        }
        var cartid = cart.getId();
//        2. Nếu đã có cart check xem có sản phẩm trong giỏ không nếu không có thì thêm mới vào cart
        // nếu có thì cập nhật số lượng
        CartItem exitcartItem = cartItemRepository.findByProductIDAndCart(
                cartid, request.getProductId()
        );
        if (exitcartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cartid);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            Product product = productRepository.detailProduct(request.getProductId());
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItemRepository.insertCartItem(cartItem);
        } else {
            exitcartItem.setQuantity(exitcartItem.getQuantity() + request.getQuantity());
            cartItemRepository.updateCartItem(exitcartItem);
        }

    }
}
