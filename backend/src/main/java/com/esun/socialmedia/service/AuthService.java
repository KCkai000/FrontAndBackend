package com.esun.socialmedia.service;

import com.esun.socialmedia.common.exception.BusinessException;
import com.esun.socialmedia.common.util.JwtUtils;
import com.esun.socialmedia.common.util.SecurityUtils;
import com.esun.socialmedia.domain.dto.request.LoginRequest;
import com.esun.socialmedia.domain.dto.request.RegisterRequest;
import com.esun.socialmedia.domain.dto.response.LoginResponse;
import com.esun.socialmedia.domain.entity.User;
import com.esun.socialmedia.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final SecurityUtils  securityUtils;
    private final JwtUtils       jwtUtils;

    public Long register(RegisterRequest req) {
        if (userRepository.existsByPhoneAndDeletedFalse(req.getPhone())) {
            throw new BusinessException(HttpStatus.CONFLICT, "此手機號碼已被註冊");
        }

        String safeEmail = req.getEmail() != null && !req.getEmail().isBlank()
                ? securityUtils.sanitize(req.getEmail()) : null;

        if (safeEmail != null && userRepository.existsByEmailAndDeletedFalse(safeEmail)) {
            throw new BusinessException(HttpStatus.CONFLICT, "此電子郵件已被使用");
        }

        String salt    = securityUtils.generateSalt();
        String hashPwd = securityUtils.hashPassword(req.getPassword(), salt);

        User user = new User();
        user.setPhone(req.getPhone());
        user.setUserName(securityUtils.sanitize(req.getUserName()));
        user.setEmail(safeEmail);
        user.setPassword(hashPwd);
        user.setSalt(salt);

        return userRepository.save(user).getUserId();
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByPhoneAndDeletedFalse(req.getPhone())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "手機號碼或密碼錯誤"));

        if (!securityUtils.verifyPassword(req.getPassword(), user.getSalt(), user.getPassword())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "手機號碼或密碼錯誤");
        }

        String token = jwtUtils.generateToken(user.getUserId(), user.getPhone());

        return LoginResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .token(token)
                .build();
    }
}

