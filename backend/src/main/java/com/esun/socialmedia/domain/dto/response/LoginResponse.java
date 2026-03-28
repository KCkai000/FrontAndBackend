package com.esun.socialmedia.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private Long   userId;
    private String userName;
    private String phone;
    private String token;
}
