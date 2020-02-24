create table t_comment
(
	id int auto_increment comment '主键id',
	parent_id int not null comment '评论所属父类型id',
	parent_type int(1) not null comment '评论所属父类型 0-问题 1-评论',
	userid int not null comment '该条评论的创建者id',
	content varchar(256) not null comment '评论的内容',
	like_count int not null comment '点赞数',
	gmt_create BIGINT not null comment '评论创建时间',
	gmt_modify BIGINT not null comment '评论修改时间',
	constraint t_comment_pk
		primary key (id)
)
comment '问题回复，以及评论回复表';
