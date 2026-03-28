package com.esun.socialmedia.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    @NotBlank(message = "發文內容不得為空")
    @Size(max = 5000, message = "發文內容不得超過 5000 字")
    private String content;

    @Size(max = 500, message = "圖片路徑不得超過 500 字")
    private String image;
}
