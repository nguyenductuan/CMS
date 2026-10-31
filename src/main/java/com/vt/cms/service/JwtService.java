package com.vt.cms.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    @Value("${security.jwt.secret:}")
    private String secret;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    /**
     * Thực hiện chức năng nghiệp vụ theo tên hàm.
     * @return Kết quả xử lý.
     */
    private Algorithm algorithm() {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET phải có ít nhất 32 byte");
        }
        return Algorithm.HMAC256(secret);
    }

    /**
     * Xác thực tài khoản hoặc kiểm tra tính hợp lệ của JWT.
     * @param userId Tham số đầu vào của hàm.
     * @param username Tham số đầu vào của hàm.
     * @param role Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    public String generateToken(Integer userId, String username, String role) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                .sign(algorithm());
    }

    /**
     * Xác thực tài khoản hoặc kiểm tra tính hợp lệ của JWT.
     * @param token Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    public DecodedJWT verify(String token) throws JWTVerificationException {
        return JWT.require(algorithm()).build().verify(token);
    }

    /**
     * Truy vấn dữ liệu theo thông tin đầu vào.
     * @return Kết quả xử lý.
     */
    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
