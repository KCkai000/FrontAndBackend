package com.esun.socialmedia.controller;

import com.esun.socialmedia.common.response.ApiResponse;
import com.esun.socialmedia.domain.dto.request.PostRequest;
import com.esun.socialmedia.domain.dto.response.PostResponse;
import com.esun.socialmedia.service.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> listPosts() {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(postId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostRequest req) {
        Long postId = postService.createPost(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("發文成功", Map.of("postId", postId)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest req) {
        postService.updatePost(postId, userId, req);
        return ResponseEntity.ok(ApiResponse.ok("編輯成功", null));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok("刪除成功", null));
    }
}
