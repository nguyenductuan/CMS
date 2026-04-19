package com.vt.cms.model.repository;

import com.vt.cms.model.entity.User;

import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface UserRepository {
    List<User> getUser();
     int createUser(User user);
     User detailUser(Integer id);
}
