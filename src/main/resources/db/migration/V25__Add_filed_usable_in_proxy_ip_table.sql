alter table t_proxy_ip
	add usable int(1) default 1 not null comment '该ip是否可用';