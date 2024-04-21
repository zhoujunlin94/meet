CREATE DATABASE IF NOT EXISTS `base` CHARACTER SET 'utf8mb4';

CREATE DATABASE IF NOT EXISTS `meet` CHARACTER SET 'utf8mb4';

USE base;

CREATE TABLE IF NOT EXISTS `cache_config`
(
    `key`   varchar(128)  NOT NULL DEFAULT '' COMMENT '配置key',
    `value` varchar(2048) NOT NULL DEFAULT '' COMMENT '配置值',
    `desc`  varchar(1024) NOT NULL DEFAULT '' COMMENT '配置描述',
    PRIMARY KEY (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='业务配置缓存表';

use meet;

CREATE TABLE IF NOT EXISTS `json_table`
(
    `id`       int auto_increment NOT NULL COMMENT '配置key',
    `json_str` varchar(2048)      NOT NULL DEFAULT '{}' COMMENT 'json',
    `json_obj` varchar(2048)      NOT NULL DEFAULT '{}' COMMENT 'json',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='json表';

CREATE TABLE IF NOT EXISTS `meet_user`
(
    `id`        int auto_increment NOT NULL COMMENT '配置key',
    `user_id`   int                NOT NULL DEFAULT 0 COMMENT '用户id',
    `user_name` varchar(255)       NOT NULL DEFAULT '' COMMENT '用户名',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='用户表';