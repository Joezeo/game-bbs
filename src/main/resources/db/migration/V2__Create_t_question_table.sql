create table t_question
(
	id int null comment '主键id',
	title varchar(100) null comment '问题标题',
	description varchar(256) null comment '问题描述',
	tag varchar(256) null comment '问题标签，以英文逗号作为分割',
	comment_count int null comment '问题的评论数',
	view_count int null comment '问题浏览数',
	like_count int null comment '问题点赞数',
	gmt_create BIGINT null comment '问题创建时间',
	gmt_modify bigint null comment '问题修改时间'
)
comment '存储提问';

