create table t_role
(
	id int auto_increment comment '主键 id Integer类型',
	name varchar(255) not null comment '角色名称',
	gmt_create BIGINT not null comment '创建时间',
	gmt_modify BIGINT not null comment '修改时间',
	constraint t_role_pk
		primary key (id)
)
comment '角色表';

create table t_user_role
(
	id int auto_increment comment '主键 id Integer类型',
	userid BIGINT not null comment '用户id long类型',
	roleid int not null comment '角色id Integer类型',
	constraint t_user_role_pk
		primary key (id)
)
comment '用户-角色映射表';

