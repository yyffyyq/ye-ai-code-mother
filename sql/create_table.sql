create database JintuWebDB;


use JintuWebDB;
-- 创建轮播图管理表
CREATE TABLE `carousel_manager` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播图编号 (主键)',
                                    `image_url` varchar(500) NOT NULL COMMENT '图片地址/路径 (前端展示必用)',
                                    `location_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '展示位置: 1-首页, 2-副页, 3-其他',
                                    `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '播放排序 (数字越小越靠前，控制轮播顺序)',
                                    `audit_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核状态: 0-待审核, 1-已通过, 2-已驳回',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间 (系统自动生成)',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 (系统自动更新)',
                                    `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0-正常, 1-已删除',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网页轮播图管理表';


use jintuwebdb;
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
    ) comment '用户' collate = utf8mb4_unicode_ci;
-- 创建轮播图位置管理表
CREATE TABLE `carousel_location` (
    `id` tinyint(4)  NOT NULL AUTO_INCREMENT COMMENT '位置ID (主鍵)',
    `name` VARCHAR(50) NOT NULL COMMENT '位置名稱 (例如：首頁、副頁、其他)',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '位置描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='輪播圖位置表';

INSERT INTO `carousel_location` (`id`, `name`, `description`) VALUES
                                                                  (0, '未分配', '默认位置'),
                                                                  (1, '首页', '首页轮播图'),
                                                                  (2, '副页', '副页轮播图'),
                                                                  (3, '其他', '其他位置');

-- 添加轮播图管理表外键约束 目标location_type-轮播图管理表（id）
ALTER TABLE carousel_manager
    ADD CONSTRAINT `fk_carousel_location_type`
        FOREIGN KEY (`location_type`)
            REFERENCES `carousel_location` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;



delete from carousel_manager;
