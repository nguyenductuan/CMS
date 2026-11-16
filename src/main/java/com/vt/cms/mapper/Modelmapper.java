package com.vt.cms.mapper;

import com.vt.cms.Shipping.domian.enity.Shipping;
import com.vt.cms.account.domain.enity.User;
import com.vt.cms.cart.application.dto.resp.CartResponse;
import com.vt.cms.cart.domain.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface Modelmapper {

    /**
     * Chuyển đổi dữ liệu convertusers. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<User> convertusers(List<User> user);



    /**
     * Chuyển đổi dữ liệu convert shipping. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    List<Shipping> convertShipping(List<Shipping> shipping);

    /**
     * Thực hiện xử lý fin by user id. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    CartResponse finByUserId(Cart cart);

    //ProductResponse convertProduct(Product product);

   // ShippingResponse convertShippingId(Shipping shipping);

    /**
     * Chuyển đổi dữ liệu convert order. Tham số và kết quả tuân theo chữ ký phương thức.
     */
//    OrderResponse convertOrder(OrderResponse order);

}
