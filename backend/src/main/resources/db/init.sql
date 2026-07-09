-- ============================================================
-- init.sql - 博客系统数据库初始化
-- 每次运行会先删除所有已有表再重新创建
-- ============================================================

DROP TABLE IF EXISTS `message`;
DROP TABLE IF EXISTS `blog`;
DROP TABLE IF EXISTS `profile`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `username`    VARCHAR(50)  NOT NULL UNIQUE,
    `password`    VARCHAR(255) NOT NULL,
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'user',
    `nickname`    VARCHAR(50)  DEFAULT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `blog` (
    `id`          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `title`       VARCHAR(200) NOT NULL,
    `content`     TEXT         NOT NULL,
    `summary`     VARCHAR(500) DEFAULT NULL,
    `category`    VARCHAR(50)  DEFAULT NULL,
    `author_id`   BIGINT       DEFAULT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `message` (
    `id`          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `nickname`    VARCHAR(50)  NOT NULL,
    `content`     TEXT         NOT NULL,
    `blog_id`     BIGINT       DEFAULT NULL,
    `user_id`     BIGINT       DEFAULT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `profile` (
    `id`          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `title`       VARCHAR(100) DEFAULT '关于我',
    `content`     TEXT         DEFAULT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;