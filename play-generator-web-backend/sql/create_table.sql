# 数据库初始化
# @author <a href="https://github.com/chenxin777">玩物志出品</a>
#

-- 创建库
create database if not exists generator;

-- 切换库
use generator;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 代码生成器
create table if not exists generator
(
    id         bigint auto_increment comment 'id' primary key,
    name      varchar(128)                       null comment '标题',
    description    text                               null comment '描述',
    basePackage   varchar(128)                    null comment '基础包',
    version       varchar(128)                    null comment '版本',
    author        varchar(128)                     null comment '作者',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    picture    varchar(256)                       null comment '图片',
    fileConfig  text                              null comment '文件配置（json字符串）',
    modelConfig     text                          null comment '模型配置（json字符串）',
    distPath        text                          null  comment '代码生成器产物路径',
    status          int  default 0                not null comment '状态',
    userId          bigint                        not null comment '创建用户id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
    ) comment '代码生成器' collate = utf8mb4_unicode_ci;

INSERT INTO user (userAccount, userPassword, userName, userAvatar, userProfile, userRole)
VALUES ('user1', 'password1', 'User One', 'http://example.com/avatar1.jpg', 'User One Profile', 'admin'),
       ('user2', 'password2', 'User Two', 'http://example.com/avatar2.jpg', 'User Two Profile', 'user'),
       ('user3', 'password3', 'User Three', 'http://example.com/avatar3.jpg', 'User Three Profile', 'user'),
       ('user4', 'password4', 'User Four', 'http://example.com/avatar4.jpg', 'User Four Profile', 'ban'),
       ('user5', 'password5', 'User Five', 'http://example.com/avatar5.jpg', 'User Five Profile', 'admin');

INSERT INTO generator (name, description, basePackage, version, author, tags, picture, fileConfig, modelConfig, distPath, userId, status)
VALUES ('Gen1', 'This is the first generator.', 'com.example.gen1', '1.0', 'John Doe', '["tag1", "tag2"]', 'http://example.com/gen1.png', '{}', '{}', '/path/to/gen1', 1, 1),
       ('Gen2', 'This is the second generator.', 'com.example.gen2', '2.0', 'Jane Smith', '["tag3", "tag4"]', 'http://example.com/gen2.png', '{}', '{}', '/path/to/gen2', 2, 1),
       ('Gen3', 'This is the third generator.', 'com.example.gen3', '3.0', 'Alice Johnson', '["tag5", "tag6"]', 'http://example.com/gen3.png', '{}', '{}', '/path/to/gen3', 3, 1),
       ('Gen4', 'This is the fourth generator.', 'com.example.gen4', '4.0', 'Bob Brown', '["tag7", "tag8"]', 'http://example.com/gen4.png', '{}', '{}', '/path/to/gen4', 4, 1),
       ('Gen5', 'This is the fifth generator.', 'com.example.gen5', '5.0', 'Charlie Davis', '["tag9", "tag10"]', 'http://example.com/gen5.png', '{}', '{}', '/path/to/gen5', 5, 1);