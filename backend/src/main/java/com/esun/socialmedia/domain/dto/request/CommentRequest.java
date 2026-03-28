package com.esun.socialmedia.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "留言內容不得為空")
    @Size(max = 2000, message = "留言內容不得超過 2000 字")
    private String content;
}
