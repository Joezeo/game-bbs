create table t_topic_like_user
(
	id BIGINT auto_increment comment '主键id',
	topicid BIGINT not null comment '帖子id',
	userid BIGINT not null comment '给帖子点赞的人的id',
	gmt_create BIGINT not null comment '该条数据创建时间',
	gmt_modify BIGINT not null comment '该条数据修改时间',
	constraint t_topic_like_user_pk
		primary key (id)
)
comment '帖子与给帖子点赞的用户的关系表';

