package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartRepository {

    int insertCart(Cart cart);

    Cart finByUserId(@Param("userId") Integer userId);
}
