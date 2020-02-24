create table t_comment_like_user
(
	id BIGINT auto_increment comment '主键id',
	commentid BIGINT not null comment '评论的id',
	userid BIGINT not null comment '点赞用户id',
	gmt_create BIGINT not null comment '数据创建时间',
	gmt_modify BIGINT not null comment '数据修改时间',
	constraint t_comment_like_user_pk
		primary key (id)
)
comment '给评论点赞的用户id与评论id的映射表';

