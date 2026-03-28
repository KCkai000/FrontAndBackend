package com.esun.socialmedia.service;

import com.esun.socialmedia.domain.dto.request.PostRequest;
import com.esun.socialmedia.domain.dto.response.PostResponse;

import java.util.List;

public interface IPostService {

    Long createPost(Long userId, PostRequest req);

    List<PostResponse> getAllPosts();

    PostResponse getPost(Long postId);

    void updatePost(Long postId, Long userId, PostRequest req);

    void deletePost(Long postId, Long userId);
}
