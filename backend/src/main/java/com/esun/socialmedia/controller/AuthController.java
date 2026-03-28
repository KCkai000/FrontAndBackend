package com.esun.socialmedia.controller;

import com.esun.socialmedia.common.response.ApiResponse;
import com.esun.socialmedia.domain.dto.request.LoginRequest;
import com.esun.socialmedia.domain.dto.request.RegisterRequest;
import com.esun.socialmedia.domain.dto.response.LoginResponse;
import com.esun.socialmedia.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(
            @Valid @RequestBody RegisterRequest req) {
        Long userId = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("註冊成功", Map.of("userId", userId)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest req) {
        LoginResponse resp = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok(resp));
    }
}
