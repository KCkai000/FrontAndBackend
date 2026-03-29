package com.example.demo.service;



import java.util.List;

import com.example.demo.model.dto.request.PostRequest;
import com.example.demo.model.dto.response.PostResponse;

public interface PostService {

    Long createPost(Long userId, PostRequest req);

    List<PostResponse> getAllPosts();

    PostResponse getPost(Long postId);

    void updatePost(Long postId, Long userId, PostRequest req);

    void deletePost(Long postId, Long userId);
}
