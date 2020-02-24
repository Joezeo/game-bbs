alter table t_user change account_id github_account_id varchar(100) not null comment '用户的github id';

alter table t_user
	add email varchar(255) not null comment '用户邮箱，用作登录';

alter table t_user
	add password varchar(255) not null comment '加密后的密码';

alter table t_user
	add salt varchar(255) not null comment '密码盐值';

