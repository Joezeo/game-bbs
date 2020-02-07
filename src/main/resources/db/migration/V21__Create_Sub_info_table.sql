create table t_steam_sub_info
(
	id int auto_increment comment '主键id',
	appid int not null comment '礼包 appid',
	name varchar(255) not null comment '礼包名称',
	developer varchar(255) not null comment '开发商名称',
	publisher varchar(255) not null comment '发行商名称',
	date varchar(255) not null comment '发行日期',
	originalPrice int not null comment '礼包价格',
	finalPrice int not null comment '礼包价格',
	contains varchar(255) not null comment '礼包包含的内容appid列表，以逗号分隔',
	gmt_create BIGINT not null comment '创建时间戳',
	gmt_modify BIGINT not null comment '修改时间戳',
	constraint t_steam_sub_info_pk
		primary key (id)
)
comment 'steam 礼包信息';