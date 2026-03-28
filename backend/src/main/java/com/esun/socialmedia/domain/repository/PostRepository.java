package com.esun.socialmedia.domain.repository;

import com.esun.socialmedia.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByPostIdAndDeletedFalse(Long postId);

    Optional<Post> findByPostIdAndUserIdAndDeletedFalse(Long postId, Long userId);

    @Query(value = """
            SELECT p.post_id, p.user_id, u.user_name, u.cover_image,
                   p.content, p.image, p.created_at, p.updated_at,
                   (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id AND c.is_deleted = 0)
            FROM posts p
            JOIN users u ON u.user_id = p.user_id AND u.is_deleted = 0
            WHERE p.is_deleted = 0
            ORDER BY p.created_at DESC
            """, nativeQuery = true)
    List<Object[]> findAllPostsRaw();

    @Query(value = """
            SELECT p.post_id, p.user_id, u.user_name, u.cover_image,
                   p.content, p.image, p.created_at, p.updated_at
            FROM posts p
            JOIN users u ON u.user_id = p.user_id AND u.is_deleted = 0
            WHERE p.post_id = :postId AND p.is_deleted = 0
            """, nativeQuery = true)
    List<Object[]> findPostByIdRaw(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.deleted = true WHERE p.postId = :postId AND p.userId = :userId AND p.deleted = false")
    int softDeletePost(@Param("postId") Long postId, @Param("userId") Long userId);
}

