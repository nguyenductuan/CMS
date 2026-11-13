package com.vt.cms.account.application.service;

import com.vt.cms.account.application.dto.LoginRequest;
import com.vt.cms.account.application.resp.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
