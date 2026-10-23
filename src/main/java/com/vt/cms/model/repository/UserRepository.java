package com.vt.cms.model.repository;

import com.vt.cms.model.entity.User;

import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface UserRepository {
    /**
     * Lấy dữ liệu get user. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<User> getUser();
     /**
      * Tạo mới create user. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
      */
     int createUser(User user);
     /**
      * Thực hiện truy cập dữ liệu detail user. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
      */
     User detailUser(Integer id);
}
