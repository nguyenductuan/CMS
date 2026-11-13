package com.vt.cms.security;

import com.vt.cms.account.application.service.JwtService;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";
    public static final String USERNAME_ATTRIBUTE = "authenticatedUsername";
    public static final String ROLE_ATTRIBUTE = "authenticatedRole";

    private static final Set<String> PROTECTED_PATHS = Set.of(

            "/v1/product/approve/",
            "/v1/product/reject/",
            "/cancelorder/",
            "/order",
            "/v1/product/",
            "/v1/order/",
            "/v1/buyer/profile",
            "/order/export",
            "/order/export-history"
    );

    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if ("/v1/auth/login".equals(path) || "/v1/user/create".equals(path)) return true;
        return PROTECTED_PATHS.stream().noneMatch(prefix ->
                prefix.endsWith("/") ? path.startsWith(prefix) : path.equals(prefix));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ") || header.substring(7).isBlank()) {
            unauthorized(response, "Vui lòng gửi JWT trong header Authorization: Bearer <token>");
            return;
        }
        try {
            DecodedJWT jwt = jwtService.verify(header.substring(7).trim());
            String role = jwt.getClaim("role").asString();
            Integer userId = jwt.getClaim("userId").asInt();
            String username = jwt.getClaim("username").asString();

            if (userId == null || username == null || role == null) {
                unauthorized(response, "JWT thiếu thông tin tài khoản hoặc quyền");
                return;
            }

            String path = request.getServletPath();
            request.setAttribute(USER_ID_ATTRIBUTE, userId);
            request.setAttribute(USERNAME_ATTRIBUTE, username);
            request.setAttribute(ROLE_ATTRIBUTE, role);

            String method = request.getMethod();

            if (!hasPermission(path, method, role)) {
                forbidden(response, "Bạn không có quyền thực hiện chức năng này");
                return;
            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException | IllegalStateException ex) {
            unauthorized(response, "JWT không hợp lệ hoặc đã hết hạn");
        }
    }
    private boolean hasPermission(String path, String method, String role) {
        if ("/v1/product/addproduct".equals(path)
                && "POST".equalsIgnoreCase(method)) {
            return "ADMIN".equals(role) || "SELLER".equals(role);
        }
        return true;
    }
    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }

    private void forbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
