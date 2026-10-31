package com.vt.cms.controller;

import com.vt.cms.model.dto.CreateUserRequest;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    /**
     * Tạo tài khoản nội bộ với quyền ADMIN hoặc SELLER.
     * Endpoint cần được giới hạn cho quản trị viên khi bật xác thực JWT.
     */
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<User>> createAccount(@Valid @RequestBody CreateUserRequest request) {
        User created = userAccountService.createAccount(request);
        return ResponseEntity.ok(new BaseResponse<>("Tạo tài khoản thành công", created));
    }
}
