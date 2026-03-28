package com.esun.socialmedia.service;

import com.esun.socialmedia.domain.dto.request.CommentRequest;
import com.esun.socialmedia.domain.dto.response.CommentResponse;

import java.util.List;

public interface ICommentService {

    Long createComment(Long userId, Long postId, CommentRequest req);

    List<CommentResponse> getComments(Long postId);
}
