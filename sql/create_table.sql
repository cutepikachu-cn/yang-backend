# 数据库初始化

-- 创建库
create database if not exists yangtuyunju;

-- 切换库
use yangtuyunju;

-- 用户表
create table if not exists user
(
    id        bigint unsigned auto_increment primary key comment 'id',
    user_account  varchar(256)                           not null comment '用户账户',
    user_password varchar(512)                           not null comment '用户密码',
    user_nickname varchar(256) comment '用户昵称',
    user_avatar   varchar(1024) comment '用户头像',
    user_profile  varchar(512) comment '用户简介',
    user_role varchar(256) default 'user' not null comment '用户角色：user/farm/admin/ban',
    create_time   datetime     default current_timestamp not null comment '创建时间',
    update_time   datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    unique key (user_account)
) comment '用户表' collate = utf8mb4_unicode_ci;

-- 帖子表
drop table if exists post;
create table if not exists post
(
    id bigint unsigned auto_increment comment 'id' primary key,
    title       varchar(512)                       not null comment '标题',
    content     text                               not null comment '内容',
    tags        varchar(1024)                      not null comment '标签列表（json 数组）',
    thumb_num   int      default 0                 not null comment '点赞数',
    favour_num  int      default 0                 not null comment '收藏数',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    index idx_user_id (user_id)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
drop table if exists post_thumb;
create table if not exists post_thumb
(
    id bigint unsigned auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (post_id),
    index idx_user_id (user_id)
) comment '帖子点赞' collate = utf8mb4_unicode_ci;

-- 帖子收藏表（硬删除）
drop table if exists post_favour;
create table if not exists post_favour
(
    id bigint unsigned auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (post_id),
    index idx_user_id (user_id)
) comment '帖子收藏' collate = utf8mb4_unicode_ci;

-- 商品表
drop table if exists commodity;
create table if not exists commodity
(
    id          bigint unsigned auto_increment comment 'id' primary key,
    user_id     bigint unsigned                                  not null comment '商品用户（羊场主）id',
    name        varchar(64)                                      not null comment '商品名称',
    img_url     varchar(512)                                     not null comment '商品图url',
    detail      text                                             not null comment '商品详情',
    visit_num   bigint unsigned        default 0                 not null comment '商品访问次数',
    share_num   bigint unsigned        default 0                 not null comment '商品分享次数',
    hot         decimal(2, 1) unsigned default 2.5               not null comment '商品热度',
    create_time datetime               default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime               default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_user_id (user_id)
) comment '商品' collate = utf8mb4_unicode_ci;
