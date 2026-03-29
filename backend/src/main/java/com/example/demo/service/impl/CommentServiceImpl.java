package com.example.demo.service.impl;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.util.SecurityUtils;
import com.example.demo.model.dto.request.CommentRequest;
import com.example.demo.model.dto.response.CommentResponse;
import com.example.demo.model.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository    postRepository;
    private final SecurityUtils     securityUtils;

    @Transactional
    public Long createComment(Long userId, Long postId, CommentRequest req) {
        if (!postRepository.existsByPostIdAndDeletedFalse(postId)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "發文不存在");
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(securityUtils.sanitize(req.getContent()));
        return commentRepository.save(comment).getCommentId();
    }

    public List<CommentResponse> getComments(Long postId) {
        return commentRepository.findCommentsByPostIdRaw(postId).stream()
                .map(r -> CommentResponse.builder()
                        .commentId(toLong(r[0]))
                        .userId(toLong(r[1]))
                        .userName((String) r[2])
                        .authorAvatar((String) r[3])
                        .content((String) r[4])
                        .postId(postId)
                        .createdAt(toLocalDateTime(r[5]))
                        .build())
                .collect(Collectors.toList());
    }

    private static Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Long l) return l;
        if (o instanceof Number n) return n.longValue();
        return null;
    }

    private static LocalDateTime toLocalDateTime(Object o) {
        if (o == null) return null;
        if (o instanceof LocalDateTime ldt) return ldt;
        if (o instanceof Timestamp ts) return ts.toLocalDateTime();
        return null;
    }
}

