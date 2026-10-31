package com.vt.cms.controller;

import com.vt.cms.model.dto.CreateUserRequest;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.service.UserAccountService;
import com.vt.cms.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
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
     * Tạo mới và lưu dữ liệu nghiệp vụ.
     * @param request Tham số phục vụ xử lý createAccount.
     * @param httpRequest Tham số phục vụ xử lý createAccount.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<User>> createAccount(@Valid @RequestBody CreateUserRequest request,
                                                              HttpServletRequest httpRequest) {
        User created = userAccountService.createAccount(request);
        return ResponseEntity.ok(new BaseResponse<>("Tạo tài khoản thành công", created));
    }
}
