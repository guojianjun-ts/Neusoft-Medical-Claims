-- 创建数据库
create schema nmc collate utf8mb4_unicode_ci;
use nmc;

-- 1.用户信息表（user）
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    id           BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    userAccount  VARCHAR(256) COMMENT '账号',
    userPassword VARCHAR(512) COMMENT '密码',
    userName     VARCHAR(256) COMMENT '用户昵称',
    userAvatar   VARCHAR(255) COMMENT '用户头像',
    Gender       TINYINT COMMENT '性别:0-男;1-女',
    Mobile       VARCHAR(20) COMMENT '联系方式',
    Email        VARCHAR(128) COMMENT '用户邮箱',
    userStatus   TINYINT COMMENT '用户状态:1-正常0-禁用',
    isDeleted    TINYINT  DEFAULT 0 COMMENT '逻辑删除标识:0-未删除;1-已删除',
    createTime   DATETIME DEFAULT CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   DATETIME DEFAULT CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE KEY uk_userAccount (userAccount),
    index idx_mobile (mobile),
    index idx_userName (userName)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '用户信息表';