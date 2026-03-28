-- ============================================================
-- Simple Social Media Platform - DDL Script
-- Database: simple_social_media
-- Charset: utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS simple_social_media
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE simple_social_media;

-- ============================================================
-- Table: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '使用者ID (PK)',
    phone        VARCHAR(20)  NOT NULL                COMMENT '手機號碼 (登入帳號)',
    user_name    VARCHAR(100) NOT NULL                COMMENT '使用者名稱',
    email        VARCHAR(255)                         COMMENT '電子郵件',
    password     VARCHAR(255) NOT NULL                COMMENT '加鹽雜湊密碼',
    salt         VARCHAR(64)  NOT NULL                COMMENT '密碼加鹽值',
    cover_image  VARCHAR(500)                         COMMENT '封面照片路徑',
    biography    TEXT                                 COMMENT '自我介紹',
    created_at   DATETIME     NOT NULL DEFAULT NOW()  COMMENT '建立時間',
    updated_at   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新時間',
    is_deleted   TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '軟刪除旗標',
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_users_phone (phone),
    UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='使用者資料表';

-- ============================================================
-- Table: posts
-- ============================================================
CREATE TABLE IF NOT EXISTS posts (
    post_id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '發文ID (PK)',
    user_id      BIGINT       NOT NULL                COMMENT '使用者ID (FK)',
    content      TEXT         NOT NULL                COMMENT '發文內容',
    image        VARCHAR(500)                         COMMENT '圖片路徑',
    created_at   DATETIME     NOT NULL DEFAULT NOW()  COMMENT '發佈時間',
    updated_at   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新時間',
    is_deleted   TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '軟刪除旗標',
    PRIMARY KEY (post_id),
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='發文資料表';

-- ============================================================
-- Table: comments
-- ============================================================
CREATE TABLE IF NOT EXISTS comments (
    comment_id   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '留言ID (PK)',
    user_id      BIGINT       NOT NULL                COMMENT '使用者ID (FK)',
    post_id      BIGINT       NOT NULL                COMMENT '發文ID (FK)',
    content      TEXT         NOT NULL                COMMENT '留言內容',
    created_at   DATETIME     NOT NULL DEFAULT NOW()  COMMENT '留言時間',
    updated_at   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新時間',
    is_deleted   TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '軟刪除旗標',
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留言資料表';

-- ============================================================
-- Indexes for performance
-- ============================================================
CREATE INDEX idx_posts_user_id     ON posts    (user_id);
CREATE INDEX idx_posts_created_at  ON posts    (created_at DESC);
CREATE INDEX idx_comments_post_id  ON comments (post_id);
CREATE INDEX idx_comments_user_id  ON comments (user_id);
