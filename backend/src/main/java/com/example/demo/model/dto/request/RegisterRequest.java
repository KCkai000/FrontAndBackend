package com.example.demo.model.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "手機號碼不得為空")
    @Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式錯誤（須為 09 開頭 10 碼）")
    private String phone;

    @NotBlank(message = "使用者名稱不得為空")
    @Size(max = 100, message = "使用者名稱不得超過 100 字")
    private String userName;

    @NotBlank(message = "密碼不得為空")
    @Size(min = 8, max = 64, message = "密碼長度需介於 8~64 字元")
    private String password;

    @Size(max = 255, message = "Email 不得超過 255 字")
    private String email;
}
