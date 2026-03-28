package com.esun.socialmedia.service;

import com.esun.socialmedia.common.exception.BusinessException;
import com.esun.socialmedia.common.util.SecurityUtils;
import com.esun.socialmedia.domain.dto.request.CommentRequest;
import com.esun.socialmedia.domain.dto.response.CommentResponse;
import com.esun.socialmedia.domain.entity.Comment;
import com.esun.socialmedia.domain.repository.CommentRepository;
import com.esun.socialmedia.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

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

