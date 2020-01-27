alter table t_notification
	add relatedName varchar(50) not null comment '通知相关问题/评论的标题/内容，只显示50的长度';

alter table t_notification
	add notifierNmae varchar(50) not null comment '通知发起者的名字';