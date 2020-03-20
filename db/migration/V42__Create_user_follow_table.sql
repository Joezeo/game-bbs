create table t_user_follow
(
	id BIGINT auto_increment comment '主键 id',
	userid BIGINT not null comment '用户id',
	follow_id BIGINT not null comment '关注的用户id',
	gmt_create BIGINT not null comment '时间戳 创建时间',
	gmt_mofify BIGINT null comment '时间戳 修改时间',
	constraint t_user_follow_pk
		primary key (id)
)
comment '关注用户表';
