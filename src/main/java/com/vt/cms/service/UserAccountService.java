package com.vt.cms.service;

import com.vt.cms.model.dto.CreateUserRequest;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.repository.UserRepository;
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
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
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
