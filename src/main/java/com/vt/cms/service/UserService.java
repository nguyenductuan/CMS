package com.vt.cms.service;

import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.UserResponse;
import com.vt.cms.req.UserReq;

import java.util.List;

public interface UserService {
     List<?> getUser();
     void createUser(UserReq userReq);
     UserResponse getDetailuser(Integer id);
}
