package com.example.demo.service;

import com.example.demo.model.dto.request.LoginRequest;
import com.example.demo.model.dto.request.RegisterRequest;
import com.example.demo.model.dto.response.LoginResponse;

public interface AuthService {

    Long register(RegisterRequest req);

    LoginResponse login(LoginRequest req);
}
