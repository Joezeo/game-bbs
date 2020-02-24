create table t_notification
(
	id BIGINT auto_increment comment '主键id',
	notifier BIGINT not null comment '该通知的发起人id',
	receiver BIGINT not null comment '该通知的接收人id',
	relatedid BIGINT not null comment '该通知相关问题/评论的id',
	relatedtype int(1) not null comment '该通知是关于问题还是评论的 1-问题 2-评论',
	status int(1) default 0 not null comment '通知状态 0-未读 1-已读',
	gmt_create BIGINT not null comment '通知创建时间',
	gmt_modify BIGINT not null comment '通知修改时间',
	constraint t_notification_pk
		primary key (id)
)
comment '消息通知表';