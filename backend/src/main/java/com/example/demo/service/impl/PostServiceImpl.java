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
import com.example.demo.model.dto.request.PostRequest;
import com.example.demo.model.dto.response.PostResponse;
import com.example.demo.model.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository    postRepository;
    private final CommentRepository commentRepository;
    private final SecurityUtils     securityUtils;

    @Transactional
    public Long createPost(Long userId, PostRequest req) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(securityUtils.sanitize(req.getContent()));
        post.setImage(req.getImage() != null ? securityUtils.sanitize(req.getImage()) : null);
        return postRepository.save(post).getPostId();
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllPostsRaw().stream()
                .map(r -> PostResponse.builder()
                        .postId(toLong(r[0]))
                        .userId(toLong(r[1]))
                        .userName((String) r[2])
                        .authorAvatar((String) r[3])
                        .content((String) r[4])
                        .image((String) r[5])
                        .createdAt(toLocalDateTime(r[6]))
                        .updatedAt(toLocalDateTime(r[7]))
                        .commentCount(r[8] instanceof Number n ? n.longValue() : 0L)
                        .build())
                .collect(Collectors.toList());
    }

    public PostResponse getPost(Long postId) {
        List<Object[]> rows = postRepository.findPostByIdRaw(postId);
        if (rows.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "發文不存在");
        Object[] r = rows.get(0);
        return PostResponse.builder()
                .postId(toLong(r[0]))
                .userId(toLong(r[1]))
                .userName((String) r[2])
                .authorAvatar((String) r[3])
                .content((String) r[4])
                .image((String) r[5])
                .createdAt(toLocalDateTime(r[6]))
                .updatedAt(toLocalDateTime(r[7]))
                .commentCount(0L)
                .build();
    }

    @Transactional
    public void updatePost(Long postId, Long userId, PostRequest req) {
        Post post = postRepository.findByPostIdAndUserIdAndDeletedFalse(postId, userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.FORBIDDEN, "無法編輯此發文（不存在或無權限）"));
        post.setContent(securityUtils.sanitize(req.getContent()));
        post.setImage(req.getImage() != null ? securityUtils.sanitize(req.getImage()) : null);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        int rows = postRepository.softDeletePost(postId, userId);
        if (rows == 0) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "無法刪除此發文（不存在或無權限）");
        }
        commentRepository.softDeleteByPostId(postId);
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

