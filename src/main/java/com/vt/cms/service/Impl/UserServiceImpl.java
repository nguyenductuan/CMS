package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.repository.UserRepository;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.UserResponse;
import com.vt.cms.req.UserReq;
import com.vt.cms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    @Transactional
    @Override
    public List<User> getUser(){
        return modelMapper.convertusers(
                userRepository.getUser());

    }
    public  void createUser(UserReq userReq){
       // var a= modelMapper.createUser(userReq);
//        var b = userRepository.createUser(a);

    }
    @Override
    public UserResponse getDetailuser(Integer id){
         return modelMapper.convertuser(
                 (userRepository.detailUser(id))
         );
    }
}
