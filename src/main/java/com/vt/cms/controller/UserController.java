package com.vt.cms.controller;

import com.vt.cms.req.UserReq;
import com.vt.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public void list_user() {
        userService.getUser();
    }

    @PostMapping("/add")
    public void add(@RequestBody UserReq userreq) {
        userService.createUser(userreq);

    }

    @GetMapping("/user/{id}")
    public void getdetailuser(@PathVariable Integer id) {
        userService.getDetailuser(id);
    }

    @DeleteMapping("deleteuser")
    public void deleteuser(@RequestBody UserReq userreq) {
    }
}
