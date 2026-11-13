package com.vt.cms.account.application.service.impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.account.application.service.AuthService;
import com.vt.cms.account.application.dto.LoginRequest;
import com.vt.cms.account.application.resp.LoginResponse;
import com.vt.cms.account.application.service.JwtService;
import com.vt.cms.account.domain.repository.UserRepository;


import com.vt.cms.account.domain.enity.User;
import com.vt.cms.base.enums.MessageCode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthServiceimpl implements AuthService {
    final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceimpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername().trim());
        if (user == null || user.getPassword() == null
                || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(MessageCode.TEN_DANG_NHAP_HOAC_MAT_KHAU_KHONG_CHINH_XAC);


        }

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds(),
                user.getId(), user.getUsername(), user.getRole());
    }
    }

