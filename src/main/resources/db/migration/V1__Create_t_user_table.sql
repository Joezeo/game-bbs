CREATE TABLE `t_user` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`name` VARCHAR ( 50 ) NOT NULL COMMENT '用户姓名',
	`account_id` VARCHAR ( 100 ) NOT NULL COMMENT 'github用户id',
	`token` CHAR ( 36 ) NOT NULL COMMENT '用于判断用户登录状态的token，分别存储在cookie和数据库中',
	`bio` VARCHAR ( 256 ) DEFAULT NULL COMMENT '用户github个人简介',
	`gmt_create` BIGINT NOT NULL COMMENT 'gmt标准时区，用户创建时间',
	`gmt_modify` BIGINT NOT NULL COMMENT 'gmt标准时区，用户修改时间',
PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储从github获取的用户数据';