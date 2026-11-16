package com.vt.cms.account.domain.repository;


import com.vt.cms.account.domain.enity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {
    List<User> getUser();
    int createUser(User user);
    User detailUser(Integer id);
    User findByUsername(@Param("username") String username);
    int updateBuyerProfile(User user);
}
