package com.esun.socialmedia.domain.repository;

import com.esun.socialmedia.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT c.comment_id, c.user_id, u.user_name, u.cover_image, c.content, c.created_at
            FROM comments c
            JOIN users u ON u.user_id = c.user_id AND u.is_deleted = 0
            WHERE c.post_id = :postId AND c.is_deleted = 0
            ORDER BY c.created_at ASC
            """, nativeQuery = true)
    List<Object[]> findCommentsByPostIdRaw(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.postId = :postId AND c.deleted = false")
    void softDeleteByPostId(@Param("postId") Long postId);
}

