package com.vt.cms.mapper;

import com.vt.cms.model.entity.Cart;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.resp.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface Modelmapper {

    List<User> convertusers(List<User> user);

    UserResponse convertuser(User user);

    List<Shipping> convertShipping(List<Shipping> shipping);

    CartResponse finByUserId(Cart cart);

    ProductResponse convertProduct(Product product);

    ShippingResponse convertShippingId(Shipping shipping);

    OrderResponse convertOrder(OrderResponse order);

}
