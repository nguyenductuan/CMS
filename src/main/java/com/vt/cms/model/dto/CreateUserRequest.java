package com.vt.cms.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 72, message = "Mật khẩu phải từ 8 đến 72 ký tự")
    private String password;

    @NotBlank(message = "Tên người dùng không được để trống")
    private String name;

    private Integer age;

    @NotNull(message = "Quyền không được để trống")
    @Pattern(regexp = "ADMIN|SELLER|BUYER", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Quyền chỉ được là ADMIN, SELLER hoặc BUYER")
    private String role;
}
