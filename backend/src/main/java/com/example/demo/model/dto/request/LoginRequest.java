package com.example.demo.model.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "手機號碼不得為空")
    @Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式錯誤")
    private String phone;

    @NotBlank(message = "密碼不得為空")
    private String password;
}
