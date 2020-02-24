create table t_proxy_ip
(
	id int auto_increment comment '主键id',
	address varchar(255) not null comment 'ip地址',
	port int not null comment '端口号',
	type varchar(10) not null comment '类型 http/https',
	speed double not null comment '速度',
	connectTime double not null comment '连接时间',
	survive varchar(10) not null comment '存活时间',
	gmt_create BIGINT not null comment '创建时间',
	gmt_modify BIGINT not null comment '修改时间',
	constraint t_proxy_ip_pk
		primary key (id)
);