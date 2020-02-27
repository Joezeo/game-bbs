create table t_spider_failed_url
(
	id int auto_increment comment '主键 id',
	url varchar(255) not null comment 'url地址',
	type varchar(255) not null comment '该app的类型',
	gmt_create BIGINT not null comment '该条记录创建时间',
	gmt_modify BIGINT not null comment '记录修改时间',
	constraint t_spider_failed_url_pk
		primary key (id)
)
comment '记录爬取信息失败的url';

