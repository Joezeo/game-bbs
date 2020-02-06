create table t_steam_game_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_game_info_pk
		primary key (id)
);

create table t_steam_bundle_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_bundle_info_pk
		primary key (id)
);

create table t_steam_demo_game_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_demo_game_info_pk
		primary key (id)
);

create table t_steam_dlc_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_dlc_info_pk
		primary key (id)
);

create table t_steam_software_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_software_info_pk
		primary key (id)
);

create table t_steam_sound_tape_info
(
	id int auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	name varchar(255) not null comment 'app name',
	imgUrl varchar(255) not null comment '介绍图地址',
	description varchar(2000) not null comment 'app简介',
	release_date varchar(255) not null comment '发行日期',
	devloper varchar(255) not null comment '开发商',
	publisher varchar(255) not null comment '发行商',
	original_price int not null comment '原始价格',
	final_price int not null comment '最终价格',
	summary varchar(255) not null comment '用户评测',
	gmt_create BIGINT not null comment '该条数据建立时间 时间戳',
	gmt_modify BIGINT not null comment '该条数据修改时间 时间戳',
	constraint t_steam_sound_tape_info_pk
		primary key (id)
);