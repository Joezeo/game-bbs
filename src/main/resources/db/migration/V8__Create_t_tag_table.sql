create table t_tag
(
	id BIGINT auto_increment comment '主键id',
	name varchar(30) not null comment '标签名称',
	category int(1) not null comment '标签分类 1-开发语言、2-平台框架、3-服务器、4-数据库、5-开发工具',
	constraint t_tag_pk
		primary key (id)
)
comment '标签数据';
