package com.example.demo.service;


import java.util.List;

import com.example.demo.model.dto.request.CommentRequest;
import com.example.demo.model.dto.response.CommentResponse;

public interface CommentService {

    Long createComment(Long userId, Long postId, CommentRequest req);

    List<CommentResponse> getComments(Long postId);
}
