create table t_ip
(
	id BIGINT auto_increment comment '主键id',
	ip varchar(36) not null comment 'ip地址',
	gmt_create BIGINT not null comment '创建数据时间，即该ip第一次访问网站的时间',
	gmt_modify BIGINT not null comment '修改数据时间，即该ip最后一次访问网站的时间',
	constraint t_ip_pk
		primary key (id)
)
comment '记录登录网站的ip地址';

create unique index t_ip_ip_uindex
	on t_ip (ip);

