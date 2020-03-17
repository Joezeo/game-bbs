create table t_user_favorite_app
(
	id BIGINT auto_increment comment '主键id',
	userid BIGINT not null comment '用户id',
	appid int not null comment 'Steam应用appid',
	type int not null comment '应用类型',
	gmt_create BIGINT not null comment '数据创建时间',
	gmt_modify BIGINT not null comment '数据更改时间',
	constraint t_user_favorite_app_pk
		primary key (id)
);
