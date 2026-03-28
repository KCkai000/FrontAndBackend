package com.esun.socialmedia.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long          commentId;
    private Long          userId;
    private Long          postId;
    private String        userName;
    private String        authorAvatar;
    private String        content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
