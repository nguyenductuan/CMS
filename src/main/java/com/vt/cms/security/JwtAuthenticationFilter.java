package com.vt.cms.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vt.cms.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";
    public static final String USERNAME_ATTRIBUTE = "authenticatedUsername";
    public static final String ROLE_ATTRIBUTE = "authenticatedRole";

    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equalsIgnoreCase(request.getMethod())
                && "/v1/user/create".equals(request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            unauthorized(response, "Thiếu JWT Bearer token");
            return;
        }
        try {
            DecodedJWT jwt = jwtService.verify(header.substring(7).trim());
            String role = jwt.getClaim("role").asString();
            Integer userId = jwt.getClaim("userId").asInt();
            String username = jwt.getClaim("username").asString();

            if (userId == null || username == null || !"ADMIN".equals(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"Chỉ ADMIN được tạo tài khoản\"}");
                return;
            }

            request.setAttribute(USER_ID_ATTRIBUTE, userId);
            request.setAttribute(USERNAME_ATTRIBUTE, username);
            request.setAttribute(ROLE_ATTRIBUTE, role);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException | IllegalStateException ex) {
            unauthorized(response, "JWT không hợp lệ hoặc đã hết hạn");
        }
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
