package com.esun.socialmedia.controller;

import com.esun.socialmedia.common.response.ApiResponse;
import com.esun.socialmedia.domain.dto.request.CommentRequest;
import com.esun.socialmedia.domain.dto.response.CommentResponse;
import com.esun.socialmedia.service.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> listComments(
            @PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.getComments(postId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest req) {
        Long commentId = commentService.createComment(userId, postId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("留言成功", Map.of("commentId", commentId)));
    }
}
