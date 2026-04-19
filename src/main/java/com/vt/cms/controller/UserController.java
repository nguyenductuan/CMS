package com.vt.cms.controller;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.UserResponse;
import com.vt.cms.req.UserReq;
import com.vt.cms.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping

public class UserController {
    @Autowired
    private UserService userService;
    //Lấy danh sách user
     @GetMapping("/list")
    public BaseResponse<?> list_user(){

         return BaseResponse .of(userService.getUser());
}
      @PostMapping("/add")
         BaseResponse<?> add(@RequestBody UserReq userreq) {
          userService.createUser(userreq);
          return BaseResponse.of(null);
      }
      @GetMapping("/user/{id}")
              public BaseResponse<UserResponse> getdetailuser(@PathVariable Integer id){
          return  BaseResponse.of(userService.getDetailuser(id));
          }

}
