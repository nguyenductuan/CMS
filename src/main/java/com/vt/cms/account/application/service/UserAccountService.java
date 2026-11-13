package com.vt.cms.account.application.service;


import com.vt.cms.Exception.BusinessException;

import com.vt.cms.account.domain.repository.UserRepository;
import com.vt.cms.account.domain.enity.User;

import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.model.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(rollbackFor = Exception.class)
    public User createAccount(CreateUserRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.findByUsername(username) != null) {
            throw new BusinessException(MessageCode.TEN_DANG_NHAP_HOAC_MAT_KHAU_KHONG_CHINH_XAC);
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setAge(request.getAge());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase(Locale.ROOT));

        if (userRepository.createUser(user) != 1 || user.getId() == null) {
            throw new IllegalStateException("Không thể tạo tài khoản");
        }
        user.setPassword(null);
        return user;
    }
}
