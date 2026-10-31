package com.vt.cms.service;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.dto.LoginRequest;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.repository.UserRepository;
import com.vt.cms.model.resp.LoginResponse;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Xác thực tài khoản hoặc kiểm tra tính hợp lệ của JWT.
     * @param request Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
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
