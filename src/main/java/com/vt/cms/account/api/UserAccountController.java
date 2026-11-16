package com.vt.cms.account.api;

import com.vt.cms.account.application.service.UserAccountService;
import com.vt.cms.account.domain.enity.User;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.model.dto.CreateUserRequest;
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
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<User>> createAccount(@Valid @RequestBody CreateUserRequest request
                                                            ) {
        User created = userAccountService.createAccount(request);
        return ResponseEntity.ok(new BaseResponse<>("Tạo tài khoản thành công", created));
    }
}
