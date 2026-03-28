package com.esun.socialmedia.service;

import com.esun.socialmedia.domain.dto.request.LoginRequest;
import com.esun.socialmedia.domain.dto.request.RegisterRequest;
import com.esun.socialmedia.domain.dto.response.LoginResponse;

public interface IAuthService {

    Long register(RegisterRequest req);

    LoginResponse login(LoginRequest req);
}
