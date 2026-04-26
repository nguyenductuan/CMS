package com.vt.cms.mapper;

import com.vt.cms.model.entity.*;
import com.vt.cms.model.resp.CartResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.model.resp.ShippingResponse;
import com.vt.cms.model.resp.UserResponse;
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

    Order convertOrder(Order order);

}
