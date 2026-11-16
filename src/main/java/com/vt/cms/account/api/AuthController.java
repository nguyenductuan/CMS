package com.vt.cms.account.api;

import com.vt.cms.account.application.service.AuthService;
import com.vt.cms.account.application.dto.LoginRequest;
import com.vt.cms.account.application.resp.LoginResponse;
import com.vt.cms.base.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Xử lý xác thực tài khoản và thông tin bảo mật.
     * @param request Tham số phục vụ xử lý login.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(new BaseResponse<>("Đăng nhập thành công", authService.login(request)));
    }
}
