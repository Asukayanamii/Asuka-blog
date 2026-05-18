CREATE DATABASE IF NOT EXISTS asuka_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE asuka_blog;

-- 专题表
CREATE TABLE IF NOT EXISTS blog_topic (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    topic_name  VARCHAR(100) NOT NULL COMMENT '专题名称',
    description VARCHAR(500) DEFAULT '' COMMENT '专题描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专题表';

-- 文章表
CREATE TABLE IF NOT EXISTS blog_article (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL COMMENT '文章标题',
    summary     VARCHAR(500) DEFAULT '' COMMENT '文章摘要',
    content_md  MEDIUMTEXT COMMENT 'Markdown原文',
    content_html MEDIUMTEXT COMMENT 'HTML渲染内容',
    topic_id    INT DEFAULT NULL COMMENT '专题ID',
    sort        INT DEFAULT 0 COMMENT '排序权重',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_topic_id (topic_id),
    KEY idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 管理员表
CREATE TABLE IF NOT EXISTS blog_admin (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 默认管理员 (密码: admin123)
INSERT INTO blog_admin (username, password) VALUES ('admin', '$2a$10$j4GyuujZji3DH6X4dgCJhevIQMXYaErEiLFKDxMDQskR4AcmWZ1NG');
